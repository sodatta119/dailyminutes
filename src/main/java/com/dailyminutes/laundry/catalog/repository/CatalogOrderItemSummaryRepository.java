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

public interface CatalogOrderItemSummaryRepository extends ListCrudRepository<CatalogOrderItemSummaryEntity, Long> {
    List<CatalogOrderItemSummaryEntity> findByCatalogId(Long catalogId);

    List<CatalogOrderItemSummaryEntity> findByOrderId(Long orderId);

    Optional<CatalogOrderItemSummaryEntity> findByOrderItemId(Long orderItemId); // Useful for updates from OrderItem events

    List<CatalogOrderItemSummaryEntity> findByCatalogType(String catalogType);

    List<CatalogOrderItemSummaryEntity> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
