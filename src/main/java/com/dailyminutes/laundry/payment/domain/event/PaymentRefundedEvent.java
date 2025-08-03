/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.domain.event;

import java.math.BigDecimal;

public record PaymentRefundedEvent(
        Long paymentId,
        Long orderId,
        BigDecimal amount
) {
}