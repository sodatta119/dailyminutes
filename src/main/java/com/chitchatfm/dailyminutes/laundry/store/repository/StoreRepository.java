package com.chitchatfm.dailyminutes.laundry.store.repository;

import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.springframework.data.repository.ListCrudRepository;

/**
 * The interface Store repository.
 */
public interface StoreRepository extends ListCrudRepository<StoreEntity, Long> {
}

