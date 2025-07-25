/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceResponse(
        Long id,
        String swipeInvoiceId,
        Long customerId,
        LocalDateTime invoiceDate,
        BigDecimal totalPrice,
        BigDecimal totalTax,
        BigDecimal totalDiscount,
        List<InvoiceItemDto> items
) {}