/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.invoice.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Invoice entity.
 */
@Table(name = "DL_INVOICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceEntity {

    @Id
    private Long id;

    private String swipeInvoiceId; // ID from the external Swipe system

    private Long customerId; // Logical link to the Customer module

    private LocalDateTime invoiceDate;

    private BigDecimal totalPrice;

    private BigDecimal totalTax;

    private BigDecimal totalDiscount;
}

