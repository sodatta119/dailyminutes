/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 27/08/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.geofence;


import java.util.List;

/**
 * The type Tookan geofences response.
 */
public record TookanGeofencesResponse(
        String message,
        int status,
        List<GeofenceRecord> data
) {}

