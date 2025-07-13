/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.manager.repository;

import com.dailyminutes.laundry.manager.domain.model.ManagerEntity;
import org.springframework.data.repository.ListCrudRepository;

/**
 * The interface Manager repository.
 */
public interface ManagerRepository extends ListCrudRepository<ManagerEntity, Long> {
}

