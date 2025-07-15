/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.laundry.geofence.domain.model.GeofenceCustomerSummaryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GeofenceCustomerSummaryRepository extends CrudRepository<GeofenceCustomerSummaryEntity, Long> {
    List<GeofenceCustomerSummaryEntity> findByGeofenceId(Long geofenceId);
    Optional<GeofenceCustomerSummaryEntity> findByCustomerId(Long customerId); // Useful for updates from Customer events
    Optional<GeofenceCustomerSummaryEntity> findByCustomerPhoneNumber(String customerPhoneNumber); // Example finder
}
