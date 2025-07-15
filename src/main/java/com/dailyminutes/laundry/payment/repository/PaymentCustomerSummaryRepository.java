/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.payment.repository; // Updated package name

import com.dailyminutes.laundry.payment.domain.model.PaymentCustomerSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentCustomerSummaryRepository extends ListCrudRepository<PaymentCustomerSummaryEntity, Long> {
    Optional<PaymentCustomerSummaryEntity> findByPaymentId(Long paymentId); // Useful for updates from Payment events
    List<PaymentCustomerSummaryEntity> findByCustomerId(Long customerId);
    Optional<PaymentCustomerSummaryEntity> findByCustomerPhoneNumber(String customerPhoneNumber);
}