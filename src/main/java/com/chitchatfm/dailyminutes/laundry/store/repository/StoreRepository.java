package com.chitchatfm.dailyminutes.laundry.store.repository;

import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Store repository.
 */
public interface StoreRepository extends ListCrudRepository<StoreEntity, Long> {
    Optional<StoreEntity> findByName(String uniqueStoreC);

    List<StoreEntity> findByManagerId(long l);
}

