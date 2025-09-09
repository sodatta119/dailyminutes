/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.dto;

import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Create order request.
 */
public record CreateOrderRequest(
        String externalId,
        Long storeId,
        @NotNull Long customerId,
        @NotNull LocalDateTime orderDate,
        @NotNull OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemDto> items
) {
}