/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.dto;


import jakarta.validation.constraints.NotBlank;

/**
 * The type Create geofence request.
 */
public record CreateGeofenceRequest(
        @NotBlank(message = "Polygon coordinates cannot be blank")
        String polygonCoordinates,
        @NotBlank(message = "Geofence type cannot be blank")
        String geofenceType,
        @NotBlank(message = "Geofence name cannot be blank")
        String name,
        boolean active
) {
}
