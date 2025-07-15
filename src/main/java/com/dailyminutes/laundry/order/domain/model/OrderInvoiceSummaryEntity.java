/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.order.domain.model; // Updated package name

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Read model for Invoice summary information relevant to the Order module.
 * This entity is populated via events from the Invoice module.
 */
@Table("DL_ORDER_INVOICE_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderInvoiceSummaryEntity {

    @Id
    private Long id;

    private Long orderId;

    private Long invoiceId;

    private LocalDateTime invoiceDate;

    private BigDecimal totalPrice;

    private BigDecimal totalTax;

    private BigDecimal totalDiscount;
}
