/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.laundry.geofence.domain.model.GeofenceCustomerSummaryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface GeofenceCustomerSummaryRepository extends ListCrudRepository<GeofenceCustomerSummaryEntity, Long> {
    List<GeofenceCustomerSummaryEntity> findByGeofenceId(Long geofenceId);

    Optional<GeofenceCustomerSummaryEntity> findByCustomerId(Long customerId); // Useful for updates from Customer events

    Optional<GeofenceCustomerSummaryEntity> findByCustomerPhoneNumber(String customerPhoneNumber); // Example finder
}
