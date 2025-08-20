/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.domain.event;


import java.math.BigDecimal;

/**
 * The type Invoice updated event.
 */
public record InvoiceUpdatedEvent(
        Long invoiceId,
        BigDecimal newTotalPrice
) {
}