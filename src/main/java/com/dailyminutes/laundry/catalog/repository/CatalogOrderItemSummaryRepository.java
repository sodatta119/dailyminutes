/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.catalog.repository; // Updated package name

import com.dailyminutes.laundry.catalog.domain.model.CatalogOrderItemSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The interface Catalog order item summary repository.
 */
public interface CatalogOrderItemSummaryRepository extends ListCrudRepository<CatalogOrderItemSummaryEntity, Long> {
    /**
     * Find by catalog id list.
     *
     * @param catalogId the catalog id
     * @return the list
     */
    List<CatalogOrderItemSummaryEntity> findByCatalogId(Long catalogId);

    /**
     * Find by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    List<CatalogOrderItemSummaryEntity> findByOrderId(Long orderId);

    /**
     * Find by order item id optional.
     *
     * @param orderItemId the order item id
     * @return the optional
     */
    Optional<CatalogOrderItemSummaryEntity> findByOrderItemId(Long orderItemId); // Useful for updates from OrderItem events

    /**
     * Find by catalog type list.
     *
     * @param catalogType the catalog type
     * @return the list
     */
    List<CatalogOrderItemSummaryEntity> findByCatalogType(String catalogType);

    /**
     * Find by order date between list.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return the list
     */
    List<CatalogOrderItemSummaryEntity> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
