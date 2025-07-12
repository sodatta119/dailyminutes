/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.order.repository;


import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderItemEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * The interface Order item repository.
 */
public interface OrderItemRepository extends ListCrudRepository<OrderItemEntity, Long> {
    /**
     * Find by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    List<OrderItemEntity> findByOrderId(Long orderId);
}

