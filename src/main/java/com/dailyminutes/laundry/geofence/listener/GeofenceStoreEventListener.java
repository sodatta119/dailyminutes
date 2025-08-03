/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.geofence.listener;

import com.dailyminutes.laundry.geofence.domain.model.GeofenceStoreSummaryEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import com.dailyminutes.laundry.geofence.repository.GeofenceStoreSummaryRepository;
import com.dailyminutes.laundry.store.domain.event.GeofenceAssignedToStoreEvent;
import com.dailyminutes.laundry.store.domain.event.StoreDeletedEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoRequestEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeofenceStoreEventListener {

    private final GeofenceStoreSummaryRepository summaryRepository;
    private final GeofenceRepository geofenceRepository; // For geofence details
    private final ApplicationEventPublisher events;

    /**
     * Step 1: Hears that a geofence was assigned and requests store details.
     */
    @ApplicationModuleListener
    public void onGeofenceAssignedToStore(GeofenceAssignedToStoreEvent event) {
        // Publish a new event to request the store's details
        events.publishEvent(new StoreInfoRequestEvent(event.storeId(), event));
    }

    /**
     * Step 2: Hears the response from the store module and creates the summary.
     */
    @ApplicationModuleListener
    public void onStoreInfoProvided(StoreInfoResponseEvent event) {
        if (event.originalEvent() instanceof GeofenceAssignedToStoreEvent) {
            GeofenceAssignedToStoreEvent originalEvent = (GeofenceAssignedToStoreEvent) event.originalEvent();

            geofenceRepository.findById(originalEvent.geofenceId()).ifPresent(geofence -> {
                GeofenceStoreSummaryEntity summary = new GeofenceStoreSummaryEntity(
                        null,
                        event.storeId(),
                        geofence.getId(),
                        event.storeName(),
                        event.storeAddress()
                );
                summaryRepository.save(summary);
            });
        }
    }

    @ApplicationModuleListener
    public void onStoreDeleted(StoreDeletedEvent event) {
        summaryRepository.findByStoreId(event.storeId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}