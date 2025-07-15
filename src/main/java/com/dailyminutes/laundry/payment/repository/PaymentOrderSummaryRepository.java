/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.payment.repository;

import com.dailyminutes.laundry.payment.domain.model.PaymentOrderSummaryEntity; // Updated import
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentOrderSummaryRepository extends ListCrudRepository<PaymentOrderSummaryEntity, Long> {
    Optional<PaymentOrderSummaryEntity> findByPaymentId(Long paymentId); // Useful for updates from Payment events
    Optional<PaymentOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events
    List<PaymentOrderSummaryEntity> findByCustomerId(Long customerId);
    List<PaymentOrderSummaryEntity> findByStoreId(Long storeId);
    List<PaymentOrderSummaryEntity> findByStatus(String status);
}
