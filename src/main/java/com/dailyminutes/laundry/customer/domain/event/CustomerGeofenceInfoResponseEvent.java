/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.customer.domain.event;

import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;

public record CustomerGeofenceInfoResponseEvent(
        Long geofenceId,
        OrderCreatedEvent originalOrderEvent
) {}