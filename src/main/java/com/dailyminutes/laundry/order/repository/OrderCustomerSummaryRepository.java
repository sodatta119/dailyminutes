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

public interface OrderCustomerSummaryRepository extends ListCrudRepository<OrderCustomerSummaryEntity, Long> {
    Optional<OrderCustomerSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    List<OrderCustomerSummaryEntity> findByCustomerId(Long customerId);

    Optional<OrderCustomerSummaryEntity> findByCustomerPhoneNumber(String customerPhoneNumber);
}

