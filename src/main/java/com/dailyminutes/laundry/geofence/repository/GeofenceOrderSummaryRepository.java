/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 14/07/25
 */
package com.dailyminutes.laundry.geofence.repository;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceOrderSummaryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GeofenceOrderSummaryRepository extends CrudRepository<GeofenceOrderSummaryEntity, Long> {
    List<GeofenceOrderSummaryEntity> findByGeofenceId(Long geofenceId);
    Optional<GeofenceOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events
    List<GeofenceOrderSummaryEntity> findByCustomerId(Long customerId); // If needed
    List<GeofenceOrderSummaryEntity> findByStoreId(Long storeId); // If needed
}

