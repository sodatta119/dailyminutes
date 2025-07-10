package com.chitchatfm.dailyminutes.laundry.store.repository;

import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}

