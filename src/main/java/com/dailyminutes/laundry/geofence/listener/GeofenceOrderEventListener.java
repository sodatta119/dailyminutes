/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.geofence.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerAddressInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerAddressInfoResponseEvent;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceOrderSummaryEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceOrderSummaryRepository;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Geofence order event listener.
 */
@Component
@RequiredArgsConstructor
public class GeofenceOrderEventListener {

    private final GeofenceOrderSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

//    /**
//     * Step 1: Hears that an order was created and ASKS the customer module for the geofence.
//     */
//    @ApplicationModuleListener
//    public void onOrderCreated(OrderCreatedEvent event) {
//        // Publish a new event to request the geofence info for the customer of this order
//        events.publishEvent(new CustomerGeofenceInfoRequestEvent(event.customerId(), event));
//    }

    /**
     * Step 2: Hears the RESPONSE from the customer module and creates the summary.
     *
     * @param orderEvent the order event
     */
    @ApplicationModuleListener
    public void onOrderPlacedInGeofence(OrderCreatedEvent orderEvent) {
        events.publishEvent(new CustomerAddressInfoRequestEvent(orderEvent.customerId(), orderEvent));
    }

    /**
     * On customer address received.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onCustomerAddressReceived(CustomerAddressInfoResponseEvent event) {
        if (event.originalEvent() instanceof OrderCreatedEvent) {
            OrderCreatedEvent orderEvent = (OrderCreatedEvent) event.originalEvent();
            GeofenceOrderSummaryEntity summary = new GeofenceOrderSummaryEntity(
                    null,
                    orderEvent.orderId(),
                    event.geofenceId(),//orderEvent.geofenceId(),
                    orderEvent.orderDate(),
                    orderEvent.status(),
                    orderEvent.totalAmount(),
                    orderEvent.customerId(),
                    orderEvent.storeId()
            );
            summaryRepository.save(summary);
        }
    }

    /**
     * On order deleted.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onOrderDeleted(OrderDeletedEvent event) {
        summaryRepository.findByOrderId(event.orderId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}