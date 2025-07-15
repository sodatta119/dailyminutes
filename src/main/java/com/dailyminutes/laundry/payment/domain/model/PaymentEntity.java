/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.payment.domain.model;

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
 * Represents a payment transaction.
 */
@Table("DL_PAYMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentEntity {

    @Id
    private Long id;

    private Long orderId;

    private Long customerId;

    private String transactionId;

    private BigDecimal amount;

    private LocalDateTime paymentDateTime;

    private PaymentStatus status;

    private PaymentMethod method;

    private String remarks;
}

