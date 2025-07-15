/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.customer.repository;

import com.dailyminutes.laundry.customer.domain.model.CustomerOrderSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Customer order summary repository.
 */
public interface CustomerOrderSummaryRepository extends ListCrudRepository<CustomerOrderSummaryEntity, Long> {
    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<CustomerOrderSummaryEntity> findByCustomerId(Long customerId);

    /**
     * Find by status list.
     *
     * @param status the status
     * @return the list
     */
    List<CustomerOrderSummaryEntity> findByStatus(String status);

    /**
     * Find by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    List<CustomerOrderSummaryEntity> findByStoreId(Long storeId);

    /**
     * Find by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    Optional<CustomerOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates
}

