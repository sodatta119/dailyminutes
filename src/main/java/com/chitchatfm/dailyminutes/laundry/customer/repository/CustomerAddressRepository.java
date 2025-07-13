package com.chitchatfm.dailyminutes.laundry.customer.repository; /**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
import com.chitchatfm.dailyminutes.laundry.customer.domain.model.CustomerAddressEntity;
import com.chitchatfm.dailyminutes.laundry.customer.domain.model.AddressType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends CrudRepository<CustomerAddressEntity, Long> {
    List<CustomerAddressEntity> findByCustomerId(Long customerId);
    Optional<CustomerAddressEntity> findByCustomerIdAndIsDefaultTrue(Long customerId);
    List<CustomerAddressEntity> findByGeofenceId(Long geofenceId);
    List<CustomerAddressEntity> findByCustomerIdAndAddressType(Long customerId, AddressType addressType);
}

