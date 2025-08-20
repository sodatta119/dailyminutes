/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.payment.repository;

import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Payment repository.
 */
public interface PaymentRepository extends ListCrudRepository<PaymentEntity, Long> {
    /**
     * Find by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    List<PaymentEntity> findByOrderId(Long orderId);

    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<PaymentEntity> findByCustomerId(Long customerId);

    /**
     * Find by transaction id optional.
     *
     * @param transactionId the transaction id
     * @return the optional
     */
    Optional<PaymentEntity> findByTransactionId(String transactionId);

    /**
     * Find by status list.
     *
     * @param status the status
     * @return the list
     */
    List<PaymentEntity> findByStatus(PaymentStatus status);

    /**
     * Find by method list.
     *
     * @param method the method
     * @return the list
     */
    List<PaymentEntity> findByMethod(PaymentMethod method);
}
