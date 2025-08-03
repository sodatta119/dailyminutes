/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TaskOrderSummaryResponse(
        Long id,
        Long taskId,
        Long orderId,
        LocalDateTime orderDate,
        String status,
        BigDecimal totalAmount,
        Long customerId,
        Long storeId
) {
}