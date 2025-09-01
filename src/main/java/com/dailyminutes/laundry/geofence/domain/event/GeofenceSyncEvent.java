/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 31/08/25
 */
package com.dailyminutes.laundry.geofence.domain.event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Geofence sync event.
 */
public record GeofenceSyncEvent(List<GeofenceSyncPayload> geofences) {

    /**
     * The type Geofence sync payload.
     */
    public record GeofenceSyncPayload(
            String externalId,           // Tookan region_id
            String name,                 // Tookan region_name
            String description,          // Tookan region_description
            String polygonCoordinates,   // stored as String "[[[lon,lat],[lon,lat],...]]"
            boolean active,              // could default to true unless Tookan says otherwise
            LocalDateTime syncedAt       // when sync happened
    ) {}
}