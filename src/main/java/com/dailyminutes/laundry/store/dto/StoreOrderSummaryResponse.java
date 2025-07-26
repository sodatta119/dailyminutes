/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StoreOrderSummaryResponse(
        Long id,
        Long storeId,
        Long orderId,
        LocalDateTime orderDate,
        String status,
        BigDecimal totalAmount,
        Long customerId
) {}