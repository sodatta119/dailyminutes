/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.invoice.domain.model; // Updated package name

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
 * Read model for Payment summary information relevant to the Invoice module.
 * This entity is populated via events from the Payment module.
 */
@Table("DL_INVOICE_PAYMENT_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoicePaymentSummaryEntity {

    @Id
    private Long id;

    private Long invoiceId;

    private Long paymentId;

    private LocalDateTime paymentDateTime;

    private BigDecimal amount;

    private String status;

    private String method;

    private String transactionId; // Denormalized from Payment module
}

