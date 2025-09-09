package com.dailyminutes.laundry.store.listener;


import com.dailyminutes.laundry.geofence.domain.event.GeofenceDeletedEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoRequestEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoResponseEvent;
import com.dailyminutes.laundry.store.domain.event.GeofenceAssignedToStoreEvent;
import com.dailyminutes.laundry.store.domain.model.StoreGeofenceSummaryEntity;
import com.dailyminutes.laundry.store.repository.StoreGeofenceSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Store geofence event listener test.
 */
@ExtendWith(MockitoExtension.class)
class StoreGeofenceEventListenerTest {

    @Mock
    private StoreGeofenceSummaryRepository summaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private StoreGeofenceEventListener listener;

    /**
     * On geofence assigned to store should publish geofence info requested event.
     */
    @Test
    void onGeofenceAssignedToStore_shouldPublishGeofenceInfoRequestedEvent() {
        // Given: An event indicating a geofence was assigned to a store
        GeofenceAssignedToStoreEvent event = new GeofenceAssignedToStoreEvent(1L, 100L);
        ArgumentCaptor<GeofenceInfoRequestEvent> captor = ArgumentCaptor.forClass(GeofenceInfoRequestEvent.class);

        // When: The listener handles the event
        listener.onGeofenceAssignedToStore(event);

        // Then: It should publish a request for the geofence's details
        verify(events).publishEvent(captor.capture());
        GeofenceInfoRequestEvent publishedEvent = captor.getValue();

        assertThat(publishedEvent.geofenceId()).isEqualTo(100L);
        // The storeId is used as the correlationId to track the request
        GeofenceAssignedToStoreEvent geofenceAddedEvent= (GeofenceAssignedToStoreEvent) publishedEvent.originalEvent();
        assertThat(geofenceAddedEvent.storeId()).isEqualTo(1L);
    }

    /**
     * On geofence info provided should create store geofence summary.
     */
    @Test
    void onGeofenceInfoProvided_shouldCreateStoreGeofenceSummary() {
        // Given: An event from the geofence module providing the necessary details
        GeofenceInfoResponseEvent event = new GeofenceInfoResponseEvent(
                100L, "Downtown Zone", "DELIVERY_ZONE", "2343",true,"", new GeofenceAssignedToStoreEvent(1L, 10L) // correlationId is the storeId
        );
        ArgumentCaptor<StoreGeofenceSummaryEntity> captor = ArgumentCaptor.forClass(StoreGeofenceSummaryEntity.class);

        // When: The listener handles the response event
        listener.onGeofenceInfoProvided(event);

        // Then: A new summary entity should be saved
        verify(summaryRepository).save(captor.capture());
        StoreGeofenceSummaryEntity savedSummary = captor.getValue();

        // And the summary should contain all the correct information
        assertThat(savedSummary.getStoreId()).isEqualTo(1L);
        assertThat(savedSummary.getGeofenceId()).isEqualTo(100L);
        assertThat(savedSummary.getGeofenceName()).isEqualTo("Downtown Zone");
        assertThat(savedSummary.getGeofenceType()).isEqualTo("DELIVERY_ZONE");
        assertThat(savedSummary.isActive()).isTrue();
    }

    /**
     * On geofence deleted should delete summary.
     */
    @Test
    void onGeofenceDeleted_shouldDeleteSummary() {
        // Given: a geofence is deleted
        GeofenceDeletedEvent event = new GeofenceDeletedEvent(100L);
        StoreGeofenceSummaryEntity summaryToDelete = new StoreGeofenceSummaryEntity(
                1L, 1L, 100L,"9876", "Downtown Zone", "DELIVERY_ZONE", true
        );
        when(summaryRepository.findByGeofenceId(100L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the deletion event
        listener.onGeofenceDeleted(event);

        // Then: The corresponding summary record should be deleted
        verify(summaryRepository).deleteById(summaryToDelete.getId());
    }
}