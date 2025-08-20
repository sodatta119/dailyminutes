/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.order.repository; // Updated package name

import com.dailyminutes.laundry.order.domain.model.OrderCustomerSummaryEntity; // Updated import
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Order customer summary repository.
 */
public interface OrderCustomerSummaryRepository extends ListCrudRepository<OrderCustomerSummaryEntity, Long> {
    /**
     * Find by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    Optional<OrderCustomerSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<OrderCustomerSummaryEntity> findByCustomerId(Long customerId);

    /**
     * Find by customer phone number optional.
     *
     * @param customerPhoneNumber the customer phone number
     * @return the optional
     */
    Optional<OrderCustomerSummaryEntity> findByCustomerPhoneNumber(String customerPhoneNumber);
}

