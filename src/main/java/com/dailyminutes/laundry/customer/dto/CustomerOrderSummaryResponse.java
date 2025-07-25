/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerOrderSummaryResponse(
        Long id,
        Long orderId,
        Long customerId,
        LocalDateTime orderDate,
        String status,
        BigDecimal totalAmount,
        Long storeId
) {}
