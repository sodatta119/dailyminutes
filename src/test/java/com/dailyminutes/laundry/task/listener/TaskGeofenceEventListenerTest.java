package com.dailyminutes.laundry.task.listener;

import com.dailyminutes.laundry.common.events.CallerEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoRequestEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoResponseEvent;
import com.dailyminutes.laundry.task.domain.event.TaskCreatedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskDeletedEvent;
import com.dailyminutes.laundry.task.domain.model.TaskGeofenceSummaryEntity;
import com.dailyminutes.laundry.task.repository.TaskGeofenceSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * The type Task geofence event listener test.
 */
@ExtendWith(MockitoExtension.class)
class TaskGeofenceEventListenerTest {

    @Mock
    private TaskGeofenceSummaryRepository summaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private TaskGeofenceEventListener listener;

    /**
     * On task created should request info for all geofences.
     */
    @Test
    void onTaskCreated_shouldRequestInfoForAllGeofences() {
        // Given: A task is created with both a source and a destination geofence
        TaskCreatedEvent event = new TaskCreatedEvent(1L, 101L, "Task Name", "PICKUP",
                "NEW", LocalDateTime.now(), "Source Address", 10L,
                "Dest Address", 20L, 301L, 401L);
        ArgumentCaptor<GeofenceInfoRequestEvent> captor = ArgumentCaptor.forClass(GeofenceInfoRequestEvent.class);

        // When: The listener handles the event
        listener.onTaskCreated(event);

        // Then: It should publish two separate requests for geofence details
        verify(events, times(2)).publishEvent(captor.capture());
        List<GeofenceInfoRequestEvent> requests = captor.getAllValues();

        // And one request should be for the source, the other for the destination
        assertThat(requests).hasSize(2);
        assertThat(requests).extracting(GeofenceInfoRequestEvent::geofenceId).containsExactlyInAnyOrder(10L, 20L);
    }

    /**
     * On task created should request info for only one geofence if other is null.
     */
    @Test
    void onTaskCreated_shouldRequestInfoForOnlyOneGeofenceIfOtherIsNull() {
        // Given: A task is created with only a source geofence
        TaskCreatedEvent event = new TaskCreatedEvent(1L, 101L, "Task Name", "PICKUP",
                "NEW", LocalDateTime.now(), "Source Address", 10L,
                "Dest Address", null, 301L, 401L);
        ArgumentCaptor<GeofenceInfoRequestEvent> captor = ArgumentCaptor.forClass(GeofenceInfoRequestEvent.class);

        // When: The listener handles the event
        listener.onTaskCreated(event);

        // Then: It should publish only one request
        verify(events, times(1)).publishEvent(captor.capture());
        assertThat(captor.getValue().geofenceId()).isEqualTo(10L);
    }

    /**
     * On geofence info provided should create summary and set source flag.
     */
    @Test
    void onGeofenceInfoProvided_shouldCreateSummaryAndSetSourceFlag() {
        // Given: A response for a SOURCE geofence
        TaskCreatedEvent originalEvent = new TaskCreatedEvent(1L, 101L, "Task Name", "PICKUP",
                "NEW", LocalDateTime.now(), "Source Address", 10L,
                "Dest Address", 20L, 301L, 401L);
        GeofenceInfoResponseEvent responseEvent = new GeofenceInfoResponseEvent(10L, "Source Zone", "SRC_TYPE", true, "POLYGON(...)", originalEvent);
        ArgumentCaptor<TaskGeofenceSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(TaskGeofenceSummaryEntity.class);

        // When: The listener handles the response
        listener.onGeofenceInfoProvided(responseEvent);

        // Then: A summary should be saved with the correct flags
        verify(summaryRepository).save(summaryCaptor.capture());
        TaskGeofenceSummaryEntity summary = summaryCaptor.getValue();

        assertThat(summary.getTaskId()).isEqualTo(1L);
        assertThat(summary.getGeofenceId()).isEqualTo(10L);
        assertThat(summary.getGeofenceName()).isEqualTo("Source Zone");
        assertThat(summary.isSource()).isTrue();
        assertThat(summary.isDestination()).isFalse();
    }

    /**
     * On geofence info provided should create summary and set destination flag.
     */
    @Test
    void onGeofenceInfoProvided_shouldCreateSummaryAndSetDestinationFlag() {
        // Given: A response for a DESTINATION geofence
        TaskCreatedEvent originalEvent = new TaskCreatedEvent(1L, 101L, "Task Name", "PICKUP",
                "NEW", LocalDateTime.now(), "Source Address", 10L,
                "Dest Address", 20L, 301L, 401L);
        GeofenceInfoResponseEvent responseEvent = new GeofenceInfoResponseEvent(20L, "Destination Zone", "DST_TYPE",  true,"POLYGON(...)", originalEvent);
        ArgumentCaptor<TaskGeofenceSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(TaskGeofenceSummaryEntity.class);

        // When: The listener handles the response
        listener.onGeofenceInfoProvided(responseEvent);

        // Then: A summary should be saved with the correct flags
        verify(summaryRepository).save(summaryCaptor.capture());
        TaskGeofenceSummaryEntity summary = summaryCaptor.getValue();

        assertThat(summary.getTaskId()).isEqualTo(1L);
        assertThat(summary.getGeofenceId()).isEqualTo(20L);
        assertThat(summary.getGeofenceName()).isEqualTo("Destination Zone");
        assertThat(summary.isSource()).isFalse();
        assertThat(summary.isDestination()).isTrue();
    }

    /**
     * On geofence info provided should do nothing when original event is wrong type.
     */
    @Test
    void onGeofenceInfoProvided_shouldDoNothing_whenOriginalEventIsWrongType() {
        // Given: A response event where the original event is not a TaskCreatedEvent
        CallerEvent otherEvent = mock(CallerEvent.class);
        GeofenceInfoResponseEvent responseEvent = new GeofenceInfoResponseEvent(10L, "Zone", "TYPE",  true, "coords", otherEvent);

        // When: The listener handles the response
        listener.onGeofenceInfoProvided(responseEvent);

        // Then: No summary should be saved
        verify(summaryRepository, never()).save(any());
    }

    /**
     * On task deleted should delete summaries for task.
     */
    @Test
    void onTaskDeleted_shouldDeleteSummariesForTask() {
        // Given: A task deletion event
        TaskDeletedEvent event = new TaskDeletedEvent(1L);
        List<TaskGeofenceSummaryEntity> summaries = List.of(new TaskGeofenceSummaryEntity(), new TaskGeofenceSummaryEntity());
        when(summaryRepository.findByTaskId(1L)).thenReturn(summaries);

        // When: The listener handles the event
        listener.onTaskDeleted(event);

        // Then: The repository is called to delete all related summaries
        verify(summaryRepository).deleteAll(summaries);
    }
}