/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.dto;

import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        Long customerId,
        String transactionId,
        BigDecimal amount,
        LocalDateTime paymentDateTime,
        PaymentStatus status,
        PaymentMethod method,
        String remarks
) {
}