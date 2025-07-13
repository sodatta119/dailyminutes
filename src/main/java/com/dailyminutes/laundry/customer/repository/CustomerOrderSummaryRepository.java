/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.customer.repository;

import com.dailyminutes.laundry.customer.domain.model.CustomerOrderSummaryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderSummaryRepository extends CrudRepository<CustomerOrderSummaryEntity, Long> {
    List<CustomerOrderSummaryEntity> findByCustomerId(Long customerId);
    List<CustomerOrderSummaryEntity> findByStatus(String status);
    List<CustomerOrderSummaryEntity> findByStoreId(Long storeId);
    Optional<CustomerOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates
}

