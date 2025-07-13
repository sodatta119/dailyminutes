/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.store.repository;

import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Store repository.
 */
public interface StoreRepository extends ListCrudRepository<StoreEntity, Long> {
    /**
     * Find by name optional.
     *
     * @param uniqueStoreC the unique store c
     * @return the optional
     */
    Optional<StoreEntity> findByName(String uniqueStoreC);

    /**
     * Find by manager id list.
     *
     * @param l the l
     * @return the list
     */
    List<StoreEntity> findByManagerId(long l);
}

