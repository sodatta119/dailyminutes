/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.dto;

public record InvoiceCustomerSummaryResponse(
        Long id,
        Long invoiceId,
        Long customerId,
        String customerName,
        String customerPhoneNumber,
        String customerEmail
) {
}