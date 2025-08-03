/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.invoice.domain.event.InvoiceCreatedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceDeletedEvent;
import com.dailyminutes.laundry.order.domain.model.OrderInvoiceSummaryEntity;
import com.dailyminutes.laundry.order.repository.OrderInvoiceSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderInvoiceEventListener {

    private final OrderInvoiceSummaryRepository summaryRepository;

    @ApplicationModuleListener
    public void onInvoiceCreated(InvoiceCreatedEvent event) {
        // The event from the invoice module contains all the necessary data.
        OrderInvoiceSummaryEntity summary = new OrderInvoiceSummaryEntity(
                null,
                event.orderId(),
                event.invoiceId(),
                event.invoiceDate(),
                event.totalPrice(),
                event.totalTax(), // totalTax - This needs to be added to the event
                event.totalDiscount()  // totalDiscount - This needs to be added to the event
        );
        summaryRepository.save(summary);
    }

    @ApplicationModuleListener
    public void onInvoiceDeleted(InvoiceDeletedEvent event) {
        // The InvoiceDeletedEvent only has invoiceId. We need to find the summary by that.
        summaryRepository.findByInvoiceId(event.invoiceId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}