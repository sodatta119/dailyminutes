/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.geofence.repository;

import com.chitchatfm.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * The interface Geofence repository.
 */
public interface GeofenceRepository extends ListCrudRepository<GeofenceEntity, Long> {
}

