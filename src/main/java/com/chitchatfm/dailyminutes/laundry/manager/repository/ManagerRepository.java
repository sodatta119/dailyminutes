package com.chitchatfm.dailyminutes.laundry.manager.repository;

import com.chitchatfm.dailyminutes.laundry.manager.domain.model.ManagerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

/**
 * The interface Manager repository.
 */
public interface ManagerRepository extends ListCrudRepository<ManagerEntity, Long> {
}

