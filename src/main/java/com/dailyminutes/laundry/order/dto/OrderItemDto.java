/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        @NotNull Long catalogId,
        @NotNull @Positive BigDecimal quantity,
        @NotNull BigDecimal itemPriceAtOrder,
        String notes
) {}