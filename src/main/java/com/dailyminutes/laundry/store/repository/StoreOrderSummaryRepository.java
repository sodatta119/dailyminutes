/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.repository; // Updated package name

import com.dailyminutes.laundry.store.domain.model.StoreOrderSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Store order summary repository.
 */
public interface StoreOrderSummaryRepository extends ListCrudRepository<StoreOrderSummaryEntity, Long> {
    /**
     * Find by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    List<StoreOrderSummaryEntity> findByStoreId(Long storeId);

    /**
     * Find by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    Optional<StoreOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<StoreOrderSummaryEntity> findByCustomerId(Long customerId);

    /**
     * Find by status list.
     *
     * @param status the status
     * @return the list
     */
    List<StoreOrderSummaryEntity> findByStatus(String status);
}
