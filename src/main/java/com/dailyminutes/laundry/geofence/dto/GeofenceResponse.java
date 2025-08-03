/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.dto;


public record GeofenceResponse(
        Long id,
        String polygonCoordinates,
        String geofenceType,
        String name,
        boolean active
) {
}