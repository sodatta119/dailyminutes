/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.repository; // Updated package name

import com.dailyminutes.laundry.store.domain.model.StoreGeofenceSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Store geofence summary repository.
 */
public interface StoreGeofenceSummaryRepository extends ListCrudRepository<StoreGeofenceSummaryEntity, Long> {
    /**
     * Find by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    List<StoreGeofenceSummaryEntity> findByStoreId(Long storeId);

    /**
     * Find by geofence id optional.
     *
     * @param geofenceId the geofence id
     * @return the optional
     */
    Optional<StoreGeofenceSummaryEntity> findByGeofenceId(Long geofenceId);
    Optional<StoreGeofenceSummaryEntity> findByGeofenceExternalId(String externalId);

    /**
     * Find by geofence type list.
     *
     * @param geofenceType the geofence type
     * @return the list
     */
    List<StoreGeofenceSummaryEntity> findByGeofenceType(String geofenceType);

    /**
     * Find by active list.
     *
     * @param active the active
     * @return the list
     */
    List<StoreGeofenceSummaryEntity> findByActive(boolean active);
}

