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


    /**
     * Find by external id optional.
     *
     * @param geofenceId the geofence id
     * @return the optional
     */
    Optional<GeofenceEntity> findByExternalId(String geofenceId);
}

