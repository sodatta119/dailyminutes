/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 04/08/25
 */
package com.dailyminutes.laundry.store.listener;

import com.dailyminutes.laundry.geofence.domain.event.GeofenceDeletedEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoRequestEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoResponseEvent;
import com.dailyminutes.laundry.store.domain.event.GeofenceAssignedToStoreEvent;
import com.dailyminutes.laundry.store.domain.model.StoreGeofenceSummaryEntity;
import com.dailyminutes.laundry.store.repository.StoreGeofenceSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Store geofence event listener.
 */
@Component
@RequiredArgsConstructor
public class StoreGeofenceEventListener {

    private final StoreGeofenceSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    /**
     * On geofence assigned to store.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onGeofenceAssignedToStore(GeofenceAssignedToStoreEvent event) {
        // Step 1: Ask the geofence module for details about the geofence.
        events.publishEvent(new GeofenceInfoRequestEvent(event.geofenceId(), event));
    }

    /**
     * On geofence info provided.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onGeofenceInfoProvided(GeofenceInfoResponseEvent event) {
        // Step 2: Receive the details and create the summary.
        if(event.originalEvent() instanceof GeofenceAssignedToStoreEvent)
        {
            GeofenceAssignedToStoreEvent geofenceAssignedEvent= (GeofenceAssignedToStoreEvent) event.originalEvent();
            Long storeId = geofenceAssignedEvent.storeId();

            StoreGeofenceSummaryEntity summary = new StoreGeofenceSummaryEntity(
                    null,
                    storeId,
                    event.geofenceId(),
                    event.geofenceName(),
                    event.geofenceType(),
                    event.active()
            );
            summaryRepository.save(summary);
        }
    }

    /**
     * On geofence deleted.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onGeofenceDeleted(GeofenceDeletedEvent event) {
        // Clean up summaries if a geofence is deleted.
        summaryRepository.findByGeofenceId(event.geofenceId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}