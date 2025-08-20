/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.dto;

/**
 * The type Task geofence summary response.
 */
public record TaskGeofenceSummaryResponse(
        Long id,
        Long taskId,
        Long geofenceId,
        String geofenceName,
        String geofenceType,
        String polygonCoordinates,
        boolean isSource,
        boolean isDestination
) {
}