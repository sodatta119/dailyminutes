/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.dto;


/**
 * The type Geofence customer summary response.
 */
public record GeofenceCustomerSummaryResponse(
        Long id,
        Long customerId,
        Long geofenceId,
        String customerName,
        String customerPhoneNumber
) {
}