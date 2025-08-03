/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.order.repository; // Updated package name

import com.dailyminutes.laundry.order.domain.model.OrderPaymentSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderPaymentSummaryRepository extends ListCrudRepository<OrderPaymentSummaryEntity, Long> {
    List<OrderPaymentSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    Optional<OrderPaymentSummaryEntity> findByPaymentId(Long paymentId); // Useful for updates from Payment events

    List<OrderPaymentSummaryEntity> findByStatus(String status);

    List<OrderPaymentSummaryEntity> findByMethod(String method);
}

