/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GeofenceOrderSummaryResponse(
        Long id,
        Long orderId,
        Long geofenceId,
        LocalDateTime orderDate,
        String status,
        BigDecimal totalAmount,
        Long customerId,
        Long storeId
) {}