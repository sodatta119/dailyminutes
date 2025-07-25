/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceCreatedEvent(
        Long invoiceId,
        Long customerId,
        LocalDateTime invoiceDate,
        BigDecimal totalPrice
) {}