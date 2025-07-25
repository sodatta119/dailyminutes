/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderPaymentSummaryResponse(
        Long id,
        Long orderId,
        Long paymentId,
        LocalDateTime paymentDateTime,
        BigDecimal amount,
        String status,
        String method,
        String transactionId
) {}