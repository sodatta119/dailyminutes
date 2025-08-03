/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/08/25
 */
package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import com.dailyminutes.laundry.order.domain.model.OrderStoreSummaryEntity;
import com.dailyminutes.laundry.order.repository.OrderStoreSummaryRepository;
import com.dailyminutes.laundry.store.domain.event.StoreInfoRequestEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoResponseEvent;
import com.dailyminutes.laundry.store.domain.event.StoreUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStoreEventListener {

    private final OrderStoreSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onOrderCreated(OrderCreatedEvent event) {
        events.publishEvent(new StoreInfoRequestEvent(event.storeId(), event));
    }

    @ApplicationModuleListener
    public void onStoreInfoProvided(StoreInfoResponseEvent event) {
        // Check if the original request was from an Order creation
        if (event.originalEvent() instanceof OrderCreatedEvent originalEvent) {
            OrderStoreSummaryEntity summary = new OrderStoreSummaryEntity(
                    null,
                    originalEvent.orderId(),
                    event.storeId(),
                    event.storeName(),
                    event.storeAddress(),
                    event.storeContactNumber(), // storeContactNumber - enrich StoreInfoProvidedEvent if needed
                    event.storeEmail()  // storeEmail - enrich StoreInfoProvidedEvent if needed
            );
            summaryRepository.save(summary);
        }
    }

    @ApplicationModuleListener
    public void onStoreUpdated(StoreUpdatedEvent event) {
        var summaries = summaryRepository.findByStoreId(event.storeId());
        summaries.forEach(summary -> {
            summary.setStoreName(event.newName());
            // Update other fields if they are included in the event
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