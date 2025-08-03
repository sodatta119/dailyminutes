/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceCreatedEvent(
        Long invoiceId,
        Long customerId,
        Long orderId,
        LocalDateTime invoiceDate,
        BigDecimal totalPrice,
        BigDecimal totalTax,
        BigDecimal totalDiscount
) implements CallerEvent {
}