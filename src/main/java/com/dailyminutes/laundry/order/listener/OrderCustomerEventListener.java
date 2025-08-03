/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/08/25
 */
package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerUpdatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import com.dailyminutes.laundry.order.domain.model.OrderCustomerSummaryEntity;
import com.dailyminutes.laundry.order.repository.OrderCustomerSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCustomerEventListener {

    private final OrderCustomerSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onOrderCreated(OrderCreatedEvent event) {
        // Ask the customer module for details, using orderId as the correlationId
        OrderCustomerSummaryEntity summary = new OrderCustomerSummaryEntity(
                null, event.orderId(), event.customerId(), "...", "...", "..."
        );
        summaryRepository.save(summary);
        events.publishEvent(new CustomerInfoRequestEvent(event.customerId(), event));
    }

    @ApplicationModuleListener
    public void onCustomerInfoProvided(CustomerInfoResponseEvent event) {
        // The correlationId is the orderId
        if(event.originalEvent() instanceof OrderCreatedEvent)
        {
            OrderCreatedEvent orderEvent= (OrderCreatedEvent) event.originalEvent();
            Long orderId = orderEvent.orderId();
            OrderCustomerSummaryEntity summary = summaryRepository.findByOrderId(orderId)
                    .orElse(new OrderCustomerSummaryEntity());

            // Populate or update all the fields with the latest data.
            summary.setOrderId(orderId);
            summary.setCustomerId(event.customerId());
            summary.setCustomerName(event.customerName());
            summary.setCustomerPhoneNumber(event.customerPhoneNumber());
            summary.setCustomerEmail(event.customerEmail());

            // Save the result. This single call handles both inserts and updates.
            summaryRepository.save(summary);
        }
    }

    @ApplicationModuleListener
    public void onCustomerUpdated(CustomerUpdatedEvent event) {
        // If customer details change, update all of their order summaries
        var summaries = summaryRepository.findByCustomerId(event.customerId());
        summaries.forEach(summary -> {
            summary.setCustomerName(event.name());
            summary.setCustomerPhoneNumber(event.phoneNumber());
            summary.setCustomerEmail(event.email());
        });
        summaryRepository.saveAll(summaries);
    }

    @ApplicationModuleListener
    public void onOrderDeleted(OrderDeletedEvent event) {
        summaryRepository.findByOrderId(event.orderId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}