/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.invoice.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * The type Invoice item entity.
 */
@Table(name = "DL_INVOICE_ITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceItemEntity {

    @Id
    private Long id;

    private Long invoiceId;

    private Long catalogId; // Logical link to the ServiceCatalog module

    private Integer quantity;

    private BigDecimal itemPrice;

    private BigDecimal tax;
}

