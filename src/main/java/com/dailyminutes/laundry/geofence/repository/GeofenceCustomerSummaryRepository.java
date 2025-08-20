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

/**
 * The interface Geofence customer summary repository.
 */
public interface GeofenceCustomerSummaryRepository extends ListCrudRepository<GeofenceCustomerSummaryEntity, Long> {
    /**
     * Find by geofence id list.
     *
     * @param geofenceId the geofence id
     * @return the list
     */
    List<GeofenceCustomerSummaryEntity> findByGeofenceId(Long geofenceId);

    /**
     * Find by customer id optional.
     *
     * @param customerId the customer id
     * @return the optional
     */
    Optional<GeofenceCustomerSummaryEntity> findByCustomerId(Long customerId); // Useful for updates from Customer events

    /**
     * Find by customer phone number optional.
     *
     * @param customerPhoneNumber the customer phone number
     * @return the optional
     */
    Optional<GeofenceCustomerSummaryEntity> findByCustomerPhoneNumber(String customerPhoneNumber); // Example finder
}
