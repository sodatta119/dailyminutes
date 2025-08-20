/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.customer.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Customer info event listener.
 */
@Component
@RequiredArgsConstructor
public class CustomerInfoEventListener {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher events;

    /**
     * On customer info requested.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onCustomerInfoRequested(CustomerInfoRequestEvent event) {
        customerRepository.findById(event.customerId()).ifPresent(customer -> {
            events.publishEvent(new CustomerInfoResponseEvent(
                    customer.getId(),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    customer.getEmail(),
                    event.originalEvent()
            ));
        });
    }
}