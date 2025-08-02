/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/08/25
 */
package com.dailyminutes.laundry.invoice.domain.listener;

import com.dailyminutes.laundry.invoice.domain.event.InvoiceCreatedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceDeletedEvent;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceOrderSummaryEntity;
import com.dailyminutes.laundry.invoice.repository.InvoiceOrderSummaryRepository;
import com.dailyminutes.laundry.order.domain.event.OrderInfoRequestEvent;
import com.dailyminutes.laundry.order.domain.event.OrderInfoResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceOrderEventListener {

    private final InvoiceOrderSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onInvoiceCreated(InvoiceCreatedEvent event) {
        events.publishEvent(new OrderInfoRequestEvent(event.orderId(), event));
    }

    @ApplicationModuleListener
    public void onOrderInfoProvided(OrderInfoResponseEvent event) {
        InvoiceCreatedEvent invoiceEvent= (InvoiceCreatedEvent) event.originalEvent();
        InvoiceOrderSummaryEntity summary = new InvoiceOrderSummaryEntity(
                null,
                invoiceEvent.invoiceId(), // This is the invoiceId
                event.orderId(),
                event.orderDate(),
                event.status(),
                event.totalAmount()
        );
        summaryRepository.save(summary);
    }

    @ApplicationModuleListener
    public void onInvoiceDeleted(InvoiceDeletedEvent event) {
        summaryRepository.findByInvoiceId(event.invoiceId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}