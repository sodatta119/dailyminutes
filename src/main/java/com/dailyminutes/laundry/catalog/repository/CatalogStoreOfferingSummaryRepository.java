/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.catalog.repository; // Updated package name

import com.dailyminutes.laundry.catalog.domain.model.CatalogStoreOfferingSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CatalogStoreOfferingSummaryRepository extends ListCrudRepository<CatalogStoreOfferingSummaryEntity, Long> {
    List<CatalogStoreOfferingSummaryEntity> findByCatalogId(Long catalogId);

    List<CatalogStoreOfferingSummaryEntity> findByStoreId(Long storeId);

    Optional<CatalogStoreOfferingSummaryEntity> findByCatalogIdAndStoreId(Long catalogId, Long storeId); // For unique offering per store

    List<CatalogStoreOfferingSummaryEntity> findByCatalogType(String catalogType);

    List<CatalogStoreOfferingSummaryEntity> findByActive(boolean active);

    List<CatalogStoreOfferingSummaryEntity> findByEffectiveToAfter(LocalDate date);
}
