/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.customer.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.customer.repository.CustomerAddressRepository;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomerInfoEventListener {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final ApplicationEventPublisher events;

    /**
     * Handles CustomerInfoRequestEvent by looking up customer + default address,
     * then publishing CustomerInfoResponseEvent.
     */
    @ApplicationModuleListener
    public void onCustomerInfoRequested(CustomerInfoRequestEvent event) {
        customerRepository.findById(event.customerId()).ifPresent(customer -> {
            // Try to fetch default address for this customer
            var addressOpt = addressRepository.findFirstByCustomerIdAndIsDefaultTrue(customer.getId());

            String addressLine = null;
            String lat = null;
            String lng = null;
            Long geofenceId = null;

            if (addressOpt.isPresent()) {
                var addr = addressOpt.get();
                addressLine = addr.getAddressLine();
                lat = addr.getLatitude();
                lng = addr.getLongitude();
                geofenceId = addr.getGeofenceId();
            }

            // Publish full response
            publishEvent(new CustomerInfoResponseEvent(
                    customer.getId(),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    customer.getEmail(),
                    addressLine,
                    lat,
                    lng,
                    geofenceId,
                    event.originalEvent() // important to pass correlation
            ));
        });
    }

    @Transactional
    private void publishEvent(CustomerInfoResponseEvent event)
    {
        events.publishEvent(event);
    }
}
