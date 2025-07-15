/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.repository; // Updated package name

import com.dailyminutes.laundry.store.domain.model.StoreOrderSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface StoreOrderSummaryRepository extends ListCrudRepository<StoreOrderSummaryEntity, Long> {
    List<StoreOrderSummaryEntity> findByStoreId(Long storeId);
    Optional<StoreOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events
    List<StoreOrderSummaryEntity> findByCustomerId(Long customerId);
    List<StoreOrderSummaryEntity> findByStatus(String status);
}
