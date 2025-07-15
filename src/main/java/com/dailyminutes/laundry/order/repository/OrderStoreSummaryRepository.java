/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.order.repository; // Updated package name

import com.dailyminutes.laundry.order.domain.model.OrderStoreSummaryEntity; // Updated import
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderStoreSummaryRepository extends ListCrudRepository<OrderStoreSummaryEntity, Long> {
    Optional<OrderStoreSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events
    List<OrderStoreSummaryEntity> findByStoreId(Long storeId);
    Optional<OrderStoreSummaryEntity> findByStoreName(String storeName);
}
