/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.customer.listener;

import com.dailyminutes.laundry.customer.domain.model.CustomerOrderSummaryEntity;
import com.dailyminutes.laundry.customer.repository.CustomerOrderSummaryRepository;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerOrderEventListener {

    private final CustomerOrderSummaryRepository summaryRepository;

    @ApplicationModuleListener
    public void onOrderCreated(OrderCreatedEvent event) {
        CustomerOrderSummaryEntity summary = new CustomerOrderSummaryEntity(
                null,
                event.orderId(),
                event.customerId(),
                event.orderDate(),
                event.status(),
                event.totalAmount(),
                event.storeId()
        );
        summaryRepository.save(summary);
    }

    @ApplicationModuleListener
    public void onOrderUpdated(OrderUpdatedEvent event) {
        summaryRepository.findByOrderId(event.orderId()).ifPresent(summary -> {
            summary.setOrderDate(event.newOrderDate());
            summary.setStatus(event.newStatus());
            summary.setTotalAmount(event.newTotalAmount());
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