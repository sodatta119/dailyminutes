/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.payment.domain.model; // Updated package name

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
 * Read model for Order summary information relevant to the Payment module.
 * This entity is populated via events from the Order module.
 */
@Table("DL_PAYMENT_ORDER_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentOrderSummaryEntity {

    @Id
    private Long id;

    private Long paymentId;

    private Long orderId;

    private LocalDateTime orderDate;

    private String status;

    private BigDecimal totalAmount;

    private Long customerId;

    private Long storeId;
}
