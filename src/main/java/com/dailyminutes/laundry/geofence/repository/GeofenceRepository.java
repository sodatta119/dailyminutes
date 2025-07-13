/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import org.springframework.data.repository.ListCrudRepository;

/**
 * The interface Geofence repository.
 */
public interface GeofenceRepository extends ListCrudRepository<GeofenceEntity, Long> {
}

