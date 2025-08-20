/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 19/08/25
 */
package com.dailyminutes.laundry.payment.domain.event;

public record PaymentDeletedEvent(
        Long paymentId
) {
}
