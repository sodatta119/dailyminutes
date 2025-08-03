/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CreateInvoiceRequest(
        @NotBlank(message = "Swipe Invoice ID cannot be blank")
        String swipeInvoiceId,
        @NotNull(message = "Order ID cannot be null")
        Long orderId,
        @NotNull(message = "Customer ID cannot be null")
        Long customerId,
        @NotNull(message = "Invoice date cannot be null")
        LocalDateTime invoiceDate,
        @NotNull(message = "Total price cannot be null")
        BigDecimal totalPrice,
        @NotNull(message = "Total tax cannot be null")
        BigDecimal totalTax,
        @NotNull(message = "Total discount cannot be null")
        BigDecimal totalDiscount,
        @Valid
        List<InvoiceItemDto> items
) {
}