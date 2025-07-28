/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.domain.event;

import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        Long storeId,
        OrderStatus status,
        BigDecimal totalAmount,
        LocalDateTime orderDate, // Added orderDate
        List<OrderItemInfo> items // Add the list of items
) {}