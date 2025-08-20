/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * The type Invoice item dto.
 */
public record InvoiceItemDto(
        Long id, // Null for new items
        @NotNull(message = "Catalog ID cannot be null")
        Long catalogId,
        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be positive")
        Integer quantity,
        @NotNull(message = "Item price cannot be null")
        BigDecimal itemPrice,
        @NotNull(message = "Tax cannot be null")
        BigDecimal tax
) {
}