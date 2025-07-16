/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.repository; // Updated package name

import com.dailyminutes.laundry.store.domain.model.StoreGeofenceSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface StoreGeofenceSummaryRepository extends ListCrudRepository<StoreGeofenceSummaryEntity, Long> {
    List<StoreGeofenceSummaryEntity> findByStoreId(Long storeId);
    Optional<StoreGeofenceSummaryEntity> findByGeofenceId(Long geofenceId); // Useful for updates from Geofence events
    List<StoreGeofenceSummaryEntity> findByGeofenceType(String geofenceType);
    List<StoreGeofenceSummaryEntity> findByActive(boolean active);
}

