package com.chitchatfm.dailyminutes.laundry.order.repository;


import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * The interface Order repository.
 */
public interface OrderRepository extends ListCrudRepository<OrderEntity, Long> {
    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<OrderEntity> findByCustomerId(String customerId);

    /**
     * Find by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    List<OrderEntity> findByStoreId(Long storeId);
}

