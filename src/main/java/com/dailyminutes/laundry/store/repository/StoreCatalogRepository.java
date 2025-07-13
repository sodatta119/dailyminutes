/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.store.repository;

import com.dailyminutes.laundry.store.domain.model.StoreCatalogEntity; // Updated import
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StoreCatalogRepository extends CrudRepository<StoreCatalogEntity, Long> { // Updated entity name
    List<StoreCatalogEntity> findByStoreId(Long storeId);
    List<StoreCatalogEntity> findByCatalogId(Long catalogId);
    Optional<StoreCatalogEntity> findByStoreIdAndCatalogId(Long storeId, Long catalogId);
}
