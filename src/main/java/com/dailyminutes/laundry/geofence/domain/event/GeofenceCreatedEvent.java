/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.domain.event;

public record GeofenceCreatedEvent(
        Long geofenceId,
        String name,
        String geofenceType,
        boolean active
) {
}