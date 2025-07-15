/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
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

/**
 * Read model for Customer summary information relevant to the Order module.
 * This entity is populated via events from the Customer module.
 */
@Table("DL_ORDER_CUSTOMER_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCustomerSummaryEntity {

    @Id
    private Long id;

    private Long orderId;

    private Long customerId;

    private String customerName;

    private String customerPhoneNumber;

    private String customerEmail;
}

