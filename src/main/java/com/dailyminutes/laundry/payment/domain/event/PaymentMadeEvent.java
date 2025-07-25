/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.domain.event;


import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import java.math.BigDecimal;

public record PaymentMadeEvent(
        Long paymentId,
        Long orderId,
        Long customerId,
        BigDecimal amount,
        PaymentMethod method
) {}