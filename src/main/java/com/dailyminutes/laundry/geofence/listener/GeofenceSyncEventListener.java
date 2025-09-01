/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 31/08/25
 */
package com.dailyminutes.laundry.geofence.listener;

/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */

import com.dailyminutes.laundry.geofence.domain.event.GeofenceSyncEvent;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * The type Geofence sync event listener.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GeofenceSyncEventListener {

    private final GeofenceRepository geofenceRepository;

    /**
     * Handle.
     *
     * @param event the event
     */
    @EventListener
    public void handle(GeofenceSyncEvent event) {
        log.info("Received GeofenceSyncEvent with {} payloads", event.geofences().size());

        event.geofences().forEach(p -> {
            GeofenceEntity entity = geofenceRepository.findByExternalId(p.externalId())
                    .orElseGet(GeofenceEntity::new);

            entity.setExternalId(p.externalId());
            entity.setName(p.name());
            entity.setPolygonCoordinates(p.polygonCoordinates());
            entity.setActive(p.active());
            entity.setExternalSyncAt(p.syncedAt() != null ? p.syncedAt() : LocalDateTime.now());
            entity.setGeofenceType(p.description());
            entity.setIsDeleted(false);

            geofenceRepository.save(entity);
        });
    }
}
