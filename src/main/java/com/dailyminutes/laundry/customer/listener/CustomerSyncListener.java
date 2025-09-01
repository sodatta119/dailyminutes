/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 01/09/25
 */
package com.dailyminutes.laundry.customer.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerSyncEvent;
import com.dailyminutes.laundry.customer.domain.model.AddressType;
import com.dailyminutes.laundry.customer.domain.model.CustomerAddressEntity;
import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.customer.repository.CustomerAddressRepository;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Customer sync listener.
 */
@Component
@RequiredArgsConstructor
public class CustomerSyncListener {

    private final CustomerRepository repo;
    private final CustomerAddressRepository addressRepo;

    /**
     * On customer synced.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onCustomerSynced(CustomerSyncEvent event) {
        var p = event.payload();

        CustomerEntity customer = repo.findBySubscriberId(p.externalId())
                .orElseGet(CustomerEntity::new);

        customer.setSubscriberId(p.externalId());
        customer.setName(p.name());
        customer.setEmail(p.email());
        customer.setPhoneNumber(p.phone());
        customer.setTimeZone(p.timeZone());
        customer.setSubscribedTms(p.subscribedAt());

        customer = repo.save(customer);

        // address upsert
        if (p.addressLine() != null || p.latitude() != null || p.longitude() != null) {
            var addr = addressRepo.findFirstByCustomerIdAndIsDefaultTrue(customer.getId())
                    .orElseGet(CustomerAddressEntity::new);

            if (addr.getId() == null) {
                addr.setCustomerId(customer.getId());
                addr.setAddressType(AddressType.HOME);
                addr.setDefault(true);
            }

            addr.setAddressLine(p.addressLine());
            addr.setLatitude(p.latitude());
            addr.setLongitude(p.longitude());

            addressRepo.save(addr);
        }
    }
}
