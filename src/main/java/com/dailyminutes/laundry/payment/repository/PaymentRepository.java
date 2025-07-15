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

public interface PaymentRepository extends ListCrudRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByOrderId(Long orderId);
    List<PaymentEntity> findByCustomerId(Long customerId);
    Optional<PaymentEntity> findByTransactionId(String transactionId);
    List<PaymentEntity> findByStatus(PaymentStatus status);
    List<PaymentEntity> findByMethod(PaymentMethod method);
}
