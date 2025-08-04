/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 04/08/25
 */
package com.dailyminutes.laundry.geofence.listener;

import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoRequestEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoResponseEvent;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeofenceInfoEventListener {

    private final GeofenceRepository geofenceRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onGeofenceInfoRequested(GeofenceInfoRequestEvent event) {
        geofenceRepository.findById(event.geofenceId()).ifPresent(geofence -> {
            events.publishEvent(new GeofenceInfoResponseEvent(
                    geofence.getId(),
                    geofence.getName(),
                    geofence.getGeofenceType(),
                    geofence.isActive(),
                    event.originalEvent() // Pass the storeId back
            ));
        });
    }
}