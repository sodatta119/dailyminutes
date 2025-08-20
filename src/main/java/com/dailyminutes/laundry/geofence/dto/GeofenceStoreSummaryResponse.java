/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.dto;


/**
 * The type Geofence store summary response.
 */
public record GeofenceStoreSummaryResponse(
        Long id,
        Long storeId,
        Long geofenceId,
        String storeName,
        String storeAddress
) {
}