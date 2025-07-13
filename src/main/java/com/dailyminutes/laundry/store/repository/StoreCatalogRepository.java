/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.store.repository;

import com.dailyminutes.laundry.store.domain.model.StoreCatalogEntity; // Updated import
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Store catalog repository.
 */
public interface StoreCatalogRepository extends CrudRepository<StoreCatalogEntity, Long> { // Updated entity name
    /**
     * Find by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    List<StoreCatalogEntity> findByStoreId(Long storeId);

    /**
     * Find by catalog id list.
     *
     * @param catalogId the catalog id
     * @return the list
     */
    List<StoreCatalogEntity> findByCatalogId(Long catalogId);

    /**
     * Find by store id and catalog id optional.
     *
     * @param storeId   the store id
     * @param catalogId the catalog id
     * @return the optional
     */
    Optional<StoreCatalogEntity> findByStoreIdAndCatalogId(Long storeId, Long catalogId);
}
