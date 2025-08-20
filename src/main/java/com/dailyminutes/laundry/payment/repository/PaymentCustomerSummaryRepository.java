/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/08/25
 */
package com.dailyminutes.laundry.payment.repository;

import com.dailyminutes.laundry.payment.domain.model.PaymentCustomerSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.Optional;

/**
 * The interface Payment customer summary repository.
 */
public interface PaymentCustomerSummaryRepository extends ListCrudRepository<PaymentCustomerSummaryEntity, Long> {

    /**
     * Find by payment id optional.
     *
     * @param paymentId the payment id
     * @return the optional
     */
    Optional<PaymentCustomerSummaryEntity> findByPaymentId(Long paymentId);

    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<PaymentCustomerSummaryEntity> findByCustomerId(Long customerId);
}