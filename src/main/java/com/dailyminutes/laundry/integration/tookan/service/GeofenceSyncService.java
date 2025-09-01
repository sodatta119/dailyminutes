/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.integration.tookan.service;


import com.dailyminutes.laundry.geofence.domain.event.GeofenceSyncEvent;
import com.dailyminutes.laundry.integration.tookan.dto.geofence.RegionPoint;
import com.dailyminutes.laundry.integration.tookan.client.TookanSyncClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * The type Geofence sync service.
 */
@Service
@RequiredArgsConstructor
public class GeofenceSyncService {

    private final TookanSyncClient tookanClient;
    private final ApplicationEventPublisher events;

    /**
     * Sync geofences.
     */
    public void syncGeofences() {
        var tookanGeofences = tookanClient.listGeofences();

        var payloads = tookanGeofences.stream()
                .map(t -> new GeofenceSyncEvent.GeofenceSyncPayload(
                        String.valueOf(t.regionId()),          // externalId
                        t.regionName(),                        // name
                        t.regionDescription(),                 // description
                        toPolygonString(t.regionData()),       // polygonCoordinates as String
                        t.isActive() == 1,                     // active
                        LocalDateTime.now()                    // syncedAt
                ))
                .collect(Collectors.toList());

        events.publishEvent(new GeofenceSyncEvent(payloads));
    }

    private String toPolygonString(java.util.List<RegionPoint> coords) {
        if (coords == null || coords.isEmpty()) {
            return "[]";
        }
        var points = coords.stream()
                .map(c -> "[" + c.y() + "," + c.x() + "]") // Tookan sends {x=lat, y=lon}, Leaflet needs [lon,lat]
                .collect(Collectors.joining(","));
        return "[[" + points + "]]"; // GeoJSON-style Polygon
    }
}
