/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.invoice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Read model for Customer summary information relevant to the Invoice module.
 * This entity is populated via events from the Customer module.
 */
@Table("DL_INVOICE_CUSTOMER_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceCustomerSummaryEntity {

    @Id
    private Long id;

    private Long invoiceId;

    private Long customerId;

    private String customerName;

    private String customerPhoneNumber;

    private String customerEmail;
}
