package com.dailyminutes.laundry.geofence.listener;

import com.dailyminutes.laundry.common.events.CallerEvent;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceStoreSummaryEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import com.dailyminutes.laundry.geofence.repository.GeofenceStoreSummaryRepository;
import com.dailyminutes.laundry.store.domain.event.GeofenceAssignedToStoreEvent;
import com.dailyminutes.laundry.store.domain.event.StoreDeletedEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoRequestEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoResponseEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeofenceStoreEventListenerTest {

    @Mock
    private GeofenceStoreSummaryRepository summaryRepository;

    @Mock
    private GeofenceRepository geofenceRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private GeofenceStoreEventListener listener;

    @Test
    void onGeofenceAssignedToStore_shouldRequestStoreInfo() {
        // Given: An event indicating a geofence has been associated with a store
        GeofenceAssignedToStoreEvent event = new GeofenceAssignedToStoreEvent(10L, 100L);
        ArgumentCaptor<StoreInfoRequestEvent> requestCaptor = ArgumentCaptor.forClass(StoreInfoRequestEvent.class);

        // When: The listener handles this initial event
        listener.onGeofenceAssignedToStore(event);

        // Then: It should publish a new event to request more details about the store
        verify(events).publishEvent(requestCaptor.capture());
        StoreInfoRequestEvent request = requestCaptor.getValue();

        assertThat(request.storeId()).isEqualTo(10L);
        // And the original event must be passed along as the payload
        assertThat(request.originalEvent()).isEqualTo(event);
    }

    @Test
    void onStoreInfoProvided_shouldCreateSummary_whenOriginalEventIsGeofenceAssignment() {
        // Given: The response event from the store module
        GeofenceAssignedToStoreEvent originalEvent = new GeofenceAssignedToStoreEvent(10L, 100L);
        StoreInfoResponseEvent event = new StoreInfoResponseEvent(10L, "Downtown Store", "123 Main St", originalEvent);

        // And the corresponding geofence entity exists
        GeofenceEntity geofenceEntity = new GeofenceEntity(100L, "POLYGON(...)", "DELIVERY_ZONE", "Downtown Zone", true);
        when(geofenceRepository.findById(100L)).thenReturn(Optional.of(geofenceEntity));

        ArgumentCaptor<GeofenceStoreSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(GeofenceStoreSummaryEntity.class);

        // When: The listener handles the response event
        listener.onStoreInfoProvided(event);

        // Then: The final summary entity should be saved
        verify(summaryRepository).save(summaryCaptor.capture());
        GeofenceStoreSummaryEntity summary = summaryCaptor.getValue();

        // And the summary should be populated with data from both the geofence and the events
        assertThat(summary.getStoreId()).isEqualTo(10L);
        assertThat(summary.getStoreName()).isEqualTo("Downtown Store");
        assertThat(summary.getStoreAddress()).isEqualTo("123 Main St");
        assertThat(summary.getGeofenceId()).isEqualTo(100L);
    }

    @Test
    void onStoreInfoProvided_shouldDoNothing_whenOriginalEventIsDifferentType() {
        // Given: A mock event that implements CallerEvent but is NOT a GeofenceAssignedToStoreEvent
        CallerEvent otherEvent = mock(CallerEvent.class);
        StoreInfoResponseEvent event = new StoreInfoResponseEvent(10L, "Downtown Store", "123 Main St", otherEvent);

        // When: The listener handles the response event
        listener.onStoreInfoProvided(event);

        // Then: The repository should never be called because the instanceof check fails
        verify(geofenceRepository, never()).findById(any());
        verify(summaryRepository, never()).save(any());
    }

    @Test
    void onStoreDeleted_shouldDeleteSummary() {
        // Given: A store deletion event
        StoreDeletedEvent event = new StoreDeletedEvent(10L);
        GeofenceStoreSummaryEntity summaryToDelete = new GeofenceStoreSummaryEntity(1L, 10L, 100L, "Downtown Store", "123 Main St");
        when(summaryRepository.findByStoreId(10L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the deletion
        listener.onStoreDeleted(event);

        // Then: The correct summary should be deleted
        verify(summaryRepository).deleteById(1L);
    }
}