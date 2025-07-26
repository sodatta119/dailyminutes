/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.dto;

public record StoreGeofenceSummaryResponse(
        Long id,
        Long storeId,
        Long geofenceId,
        String geofenceName,
        String geofenceType,
        boolean active
) {}