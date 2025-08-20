/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/08/25
 */
package com.dailyminutes.laundry.payment.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerDeletedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentDeletedEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.model.PaymentCustomerSummaryEntity;
import com.dailyminutes.laundry.payment.repository.PaymentCustomerSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Payment customer event listener.
 */
@Component
@RequiredArgsConstructor
public class PaymentCustomerEventListener {

    private final PaymentCustomerSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    /**
     * On payment made.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onPaymentMade(PaymentMadeEvent event) {
        // Step 1: A payment was made, so ask the customer module for details.
        events.publishEvent(new CustomerInfoRequestEvent(event.customerId(), event));
    }

    /**
     * On customer info provided.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onCustomerInfoProvided(CustomerInfoResponseEvent event) {
        // Step 2: Receive the response and check if it was triggered by our event.
        if (event.originalEvent() instanceof PaymentMadeEvent paymentEvent) {
            PaymentCustomerSummaryEntity summary = new PaymentCustomerSummaryEntity(
                    null,
                    paymentEvent.paymentId(),
                    event.customerId(),
                    event.customerName(),
                    event.customerPhoneNumber(),
                    event.customerEmail()
            );
            summaryRepository.save(summary);
        }
    }

    /**
     * On payment deleted.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onPaymentDeleted(PaymentDeletedEvent event) {
        summaryRepository.findByPaymentId(event.paymentId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId()));
    }

    /**
     * On customer deleted.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onCustomerDeleted(CustomerDeletedEvent event) {
        var summariesToDelete = summaryRepository.findByCustomerId(event.customerId());
        summaryRepository.deleteAll(summariesToDelete);
    }
}