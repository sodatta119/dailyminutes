/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 31/07/25
 */
package com.dailyminutes.laundry.invoice.domain.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerDeletedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceCreatedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceDeletedEvent;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceCustomerSummaryEntity;
import com.dailyminutes.laundry.invoice.repository.InvoiceCustomerSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceCustomerEventListener {

    private final InvoiceCustomerSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    /**
     * Step 1: Hears an invoice was created and ASKS the customer module for details.
     */
    @ApplicationModuleListener
    public void onInvoiceCreated(InvoiceCreatedEvent event) {
        if (event.customerId() != null) {
            events.publishEvent(new CustomerInfoRequestEvent(event.customerId(), event));
        }
    }

    /**
     * Step 2: Hears the response from the customer module and CREATES the summary.
     */
    @ApplicationModuleListener
    public void onCustomerInfoProvided(CustomerInfoResponseEvent event) {
        if(event.originalEvent() instanceof InvoiceCreatedEvent)
        {
            InvoiceCreatedEvent originalEvent= (InvoiceCreatedEvent) event.originalEvent();
            InvoiceCustomerSummaryEntity summary = new InvoiceCustomerSummaryEntity(
                    null,
                    originalEvent.invoiceId(), // This is the invoiceId
                    event.customerId(),
                    event.customerName(),
                    event.customerPhoneNumber(),
                    event.customerEmail()
            );
            summaryRepository.save(summary);
        }
    }

    @ApplicationModuleListener
    public void onInvoiceDeleted(InvoiceDeletedEvent event) {
        summaryRepository.findByInvoiceId(event.invoiceId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }

    @ApplicationModuleListener
    public void onCustomerDeleted(CustomerDeletedEvent event) {
        var summariesToDelete = summaryRepository.findByCustomerId(event.customerId());
        summaryRepository.deleteAll(summariesToDelete);
    }
}