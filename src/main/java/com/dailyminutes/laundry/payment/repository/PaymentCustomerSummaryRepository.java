/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/08/25
 */
package com.dailyminutes.laundry.payment.repository;

import com.dailyminutes.laundry.payment.domain.model.PaymentCustomerSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentCustomerSummaryRepository extends ListCrudRepository<PaymentCustomerSummaryEntity, Long> {

    Optional<PaymentCustomerSummaryEntity> findByPaymentId(Long paymentId);

    List<PaymentCustomerSummaryEntity> findByCustomerId(Long customerId);
}