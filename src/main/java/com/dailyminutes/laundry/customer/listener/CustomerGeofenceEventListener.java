/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.customer.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerGeofenceInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerGeofenceInfoResponseEvent;
import com.dailyminutes.laundry.customer.repository.CustomerAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerGeofenceEventListener {

    private final CustomerAddressRepository addressRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onGeofenceInfoRequested(CustomerGeofenceInfoRequestEvent event) {
        // Find the customer's default address to get their geofence
        addressRepository.findByCustomerIdAndIsDefaultTrue(event.customerId())
                .ifPresent(address -> {
                    // Publish the response, passing the geofenceId and the original event payload
                    events.publishEvent(new CustomerGeofenceInfoResponseEvent(
                            address.getGeofenceId(),
                            event.originalOrderEvent()
                    ));
                });
    }
}