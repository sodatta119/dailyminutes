/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.domain.event;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentMadeEvent(
        Long paymentId,
        Long orderId,
        Long customerId,
        BigDecimal amount,
        String method,
        String transactionId,
        LocalDateTime paymentDateTime
) {}