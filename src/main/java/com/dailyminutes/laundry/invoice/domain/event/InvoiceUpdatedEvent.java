/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.domain.event;


import java.math.BigDecimal;

public record InvoiceUpdatedEvent(
        Long invoiceId,
        BigDecimal newTotalPrice
) {
}