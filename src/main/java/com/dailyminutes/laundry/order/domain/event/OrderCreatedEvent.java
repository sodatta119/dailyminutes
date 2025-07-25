/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.domain.event;

import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        Long storeId,
        OrderStatus status,
        BigDecimal totalAmount
) {}