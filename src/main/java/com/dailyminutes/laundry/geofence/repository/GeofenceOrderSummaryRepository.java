/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 14/07/25
 */
package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.laundry.geofence.domain.model.GeofenceOrderSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Geofence order summary repository.
 */
public interface GeofenceOrderSummaryRepository extends ListCrudRepository<GeofenceOrderSummaryEntity, Long> {
    /**
     * Find by geofence id list.
     *
     * @param geofenceId the geofence id
     * @return the list
     */
    List<GeofenceOrderSummaryEntity> findByGeofenceId(Long geofenceId);

    /**
     * Find by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    Optional<GeofenceOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<GeofenceOrderSummaryEntity> findByCustomerId(Long customerId); // If needed

    /**
     * Find by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    List<GeofenceOrderSummaryEntity> findByStoreId(Long storeId); // If needed
}

