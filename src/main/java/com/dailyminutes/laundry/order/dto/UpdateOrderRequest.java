/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.dto;

import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Update order request.
 */
public record UpdateOrderRequest(
        @NotNull Long id,
        @NotNull Long storeId,
        @NotNull Long customerId,
        @NotNull LocalDateTime orderDate,
        @NotNull OrderStatus status,
        @NotNull BigDecimal totalAmount,
        @Valid List<OrderItemDto> items
) {
}