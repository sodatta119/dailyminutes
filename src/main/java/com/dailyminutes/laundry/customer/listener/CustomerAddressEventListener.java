/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.customer.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerAddressInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerAddressInfoResponseEvent;
import com.dailyminutes.laundry.customer.repository.CustomerAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Customer address event listener.
 */
@Component
@RequiredArgsConstructor
public class CustomerAddressEventListener {

    private final CustomerAddressRepository customerAddressRepository;
    private final ApplicationEventPublisher events;

    /**
     * On customer info requested.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onCustomerInfoRequested(CustomerAddressInfoRequestEvent event) {
        customerAddressRepository.findById(event.customerId()).ifPresent(address -> {
            events.publishEvent(new CustomerAddressInfoResponseEvent(
                    address.getCustomerId(),
                    address.getAddressLine(),
                    address.getLatitude(),
                    address.getLongitude(),
                    address.getGeofenceId(),
                    event.originalEvent()
            ));
        });
    }
}