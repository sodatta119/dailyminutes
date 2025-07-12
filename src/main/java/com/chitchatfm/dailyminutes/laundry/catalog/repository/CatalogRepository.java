/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */

package com.chitchatfm.dailyminutes.laundry.catalog.repository;

import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import org.springframework.data.repository.ListCrudRepository;

/**
 * The interface Catalog repository.
 */
public interface CatalogRepository extends ListCrudRepository<CatalogEntity, Long> {
}

