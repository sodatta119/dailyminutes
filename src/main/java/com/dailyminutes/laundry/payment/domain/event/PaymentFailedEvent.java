/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.domain.event;

public record PaymentFailedEvent(
        Long paymentId,
        Long orderId,
        String reason
) {}