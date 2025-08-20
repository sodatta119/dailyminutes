/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/08/25
 */
package com.dailyminutes.laundry.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The type Payment customer summary entity.
 */
@Table("DL_PAYMENT_CUSTOMER_SUMMARY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentCustomerSummaryEntity {

    @Id
    private Long id;
    private Long paymentId;
    private Long customerId;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
}