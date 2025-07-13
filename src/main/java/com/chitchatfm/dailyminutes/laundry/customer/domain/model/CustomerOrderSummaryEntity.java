/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
package com.chitchatfm.dailyminutes.laundry.customer.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Read model for Order summary information relevant to the Customer module.
 * This entity is populated via events from the Order module.
 */
@Table("DL_CUSTOMER_ORDER_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerOrderSummaryEntity {

    @Id // Auto-generated primary key for this summary record
    private Long id;

    private Long orderId;

    private Long customerId;

    private LocalDateTime orderDate;

    private String status;

    private BigDecimal totalAmount;

    private Long storeId;
}

