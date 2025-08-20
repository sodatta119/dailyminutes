/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Payment order summary response.
 */
public record PaymentOrderSummaryResponse(
        Long id,
        Long paymentId,
        Long orderId,
        LocalDateTime orderDate,
        String status,
        BigDecimal totalAmount,
        Long customerId,
        Long storeId
) {
}