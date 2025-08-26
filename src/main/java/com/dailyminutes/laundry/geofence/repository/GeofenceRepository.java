/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * The interface Geofence repository.
 */
public interface GeofenceRepository extends ListCrudRepository<GeofenceEntity, Long> {


    Optional<GeofenceEntity> findByExternalId(String geofenceId);
}

