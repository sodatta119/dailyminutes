package com.chitchatfm.dailyminutes.laundry.order.repository;

import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderItemMetadataEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * The interface Order item metadata repository.
 */
public interface OrderItemMetadataRepository extends ListCrudRepository<OrderItemMetadataEntity, Long> {
    /**
     * Find by order item id list.
     *
     * @param orderItemId the order item id
     * @return the list
     */
    List<OrderItemMetadataEntity> findByOrderItemId(Long orderItemId);
}

