package com.dailyminutes.laundry.customer.repository; /**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 13/07/25
 */

import com.dailyminutes.laundry.customer.domain.model.AddressType;
import com.dailyminutes.laundry.customer.domain.model.CustomerAddressEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Customer address repository.
 */
public interface CustomerAddressRepository extends ListCrudRepository<CustomerAddressEntity, Long> {
    /**
     * Find by customer id list.
     *
     * @param customerId the customer id
     * @return the list
     */
    List<CustomerAddressEntity> findByCustomerId(Long customerId);

    /**
     * Find by customer id and is default true optional.
     *
     * @param customerId the customer id
     * @return the optional
     */
    Optional<CustomerAddressEntity> findByCustomerIdAndIsDefaultTrue(Long customerId);

    /**
     * Find by geofence id list.
     *
     * @param geofenceId the geofence id
     * @return the list
     */
    List<CustomerAddressEntity> findByGeofenceId(Long geofenceId);

    /**
     * Find by customer id and address type list.
     *
     * @param customerId  the customer id
     * @param addressType the address type
     * @return the list
     */
    List<CustomerAddressEntity> findByCustomerIdAndAddressType(Long customerId, AddressType addressType);
}

