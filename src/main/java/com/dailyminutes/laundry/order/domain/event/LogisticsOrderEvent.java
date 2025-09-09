/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 03/09/25
 */
package com.dailyminutes.laundry.order.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

public record LogisticsOrderEvent(
        Long orderId,
        CallerEvent originalEvent // The original invoiceId
) implements CallerEvent {
}