/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentInvoiceSummaryResponse(
        Long id,
        Long paymentId,
        Long invoiceId,
        LocalDateTime invoiceDate,
        BigDecimal totalPrice,
        BigDecimal totalTax,
        BigDecimal totalDiscount
) {}