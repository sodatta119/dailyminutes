/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.order.repository; // Updated package name

import com.dailyminutes.laundry.order.domain.model.OrderPaymentSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Order payment summary repository.
 */
public interface OrderPaymentSummaryRepository extends ListCrudRepository<OrderPaymentSummaryEntity, Long> {
    /**
     * Find by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    List<OrderPaymentSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    /**
     * Find by payment id optional.
     *
     * @param paymentId the payment id
     * @return the optional
     */
    Optional<OrderPaymentSummaryEntity> findByPaymentId(Long paymentId); // Useful for updates from Payment events

    /**
     * Find by status list.
     *
     * @param status the status
     * @return the list
     */
    List<OrderPaymentSummaryEntity> findByStatus(String status);

    /**
     * Find by method list.
     *
     * @param method the method
     * @return the list
     */
    List<OrderPaymentSummaryEntity> findByMethod(String method);
}

