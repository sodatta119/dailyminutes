/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 05/08/25
 */
package com.dailyminutes.laundry.store.listener;

import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderUpdatedEvent;
import com.dailyminutes.laundry.store.domain.model.StoreOrderSummaryEntity;
import com.dailyminutes.laundry.store.repository.StoreOrderSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreOrderEventListener {

    private final StoreOrderSummaryRepository summaryRepository;

    @ApplicationModuleListener
    public void onOrderCreated(OrderCreatedEvent event) {
        StoreOrderSummaryEntity summary = new StoreOrderSummaryEntity(
                null,
                event.storeId(),
                event.orderId(),
                event.orderDate(),
                event.status(),
                event.totalAmount(),
                event.customerId()
        );
        summaryRepository.save(summary);
    }

    @ApplicationModuleListener
    public void onOrderUpdated(OrderUpdatedEvent event) {
        summaryRepository.findByOrderId(event.orderId()).ifPresent(summary -> {
            summary.setOrderDate(event.newOrderDate());
            summary.setStatus(event.newStatus());
            summary.setTotalAmount(event.newTotalAmount());
            summary.setCustomerId(event.customerId());
            summaryRepository.save(summary);
        });
    }

    @ApplicationModuleListener
    public void onOrderDeleted(OrderDeletedEvent event) {
        summaryRepository.findByOrderId(event.orderId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}