/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceOrderSummaryResponse(
        Long id,
        Long invoiceId,
        Long orderId,
        LocalDateTime orderDate,
        String status,
        BigDecimal totalAmount
) {}