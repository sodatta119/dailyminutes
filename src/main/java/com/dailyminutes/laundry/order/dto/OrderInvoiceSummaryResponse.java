/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderInvoiceSummaryResponse(
        Long id,
        Long orderId,
        Long invoiceId,
        LocalDateTime invoiceDate,
        BigDecimal totalPrice,
        BigDecimal totalTax,
        BigDecimal totalDiscount
) {}