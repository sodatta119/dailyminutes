/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Invoice payment summary response.
 */
public record InvoicePaymentSummaryResponse(
        Long id,
        Long invoiceId,
        Long paymentId,
        LocalDateTime paymentDateTime,
        BigDecimal amount,
        String status,
        String method,
        String transactionId
) {
}