/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.dto;

import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Update payment request.
 */
public record UpdatePaymentRequest(
        @NotNull Long id,
        @NotNull Long orderId,
        @NotNull Long customerId,
        @NotBlank String transactionId,
        @NotNull BigDecimal amount,
        @NotNull LocalDateTime paymentDateTime,
        @NotNull PaymentStatus status,
        @NotNull PaymentMethod method,
        String remarks
) {
}