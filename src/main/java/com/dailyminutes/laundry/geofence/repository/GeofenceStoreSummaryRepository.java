/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 14/07/25
 */
package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.laundry.geofence.domain.model.GeofenceStoreSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface GeofenceStoreSummaryRepository extends ListCrudRepository<GeofenceStoreSummaryEntity, Long> {
    Optional<GeofenceStoreSummaryEntity> findByStoreId(Long storeId); // Useful for updates from Store events
    List<GeofenceStoreSummaryEntity> findByGeofenceId(Long geofenceId); // Added: Find summaries by Geofence ID
    Optional<GeofenceStoreSummaryEntity> findByStoreName(String storeName); // Example finder
    List<GeofenceStoreSummaryEntity> findByStoreAddressContaining(String addressPart); // Example finder
}
