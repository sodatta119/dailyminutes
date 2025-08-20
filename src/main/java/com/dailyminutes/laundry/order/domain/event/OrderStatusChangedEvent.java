/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.domain.event;

import com.dailyminutes.laundry.order.domain.model.OrderStatus;

/**
 * The type Order status changed event.
 */
public record OrderStatusChangedEvent(
        Long orderId,
        OrderStatus oldStatus,
        OrderStatus newStatus
) {
}