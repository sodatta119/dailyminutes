/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 03/08/25
 */
package com.dailyminutes.laundry.payment.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.model.PaymentCustomerSummaryEntity;
import com.dailyminutes.laundry.payment.repository.PaymentCustomerSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentCustomerEventListener {

    private final PaymentCustomerSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onPaymentMade(PaymentMadeEvent event) {
        // Ask the customer module for details, using paymentId as the correlation ID
        events.publishEvent(new CustomerInfoRequestEvent(event.customerId(), event));
    }

    @ApplicationModuleListener
    public void onCustomerInfoProvided(CustomerInfoResponseEvent event) {
        // The correlationId is the paymentId
        if(event.originalEvent() instanceof PaymentMadeEvent)
        {
            PaymentMadeEvent paymentEvent= (PaymentMadeEvent) event.originalEvent();
            Long paymentId = paymentEvent.paymentId();

            // Check if a summary for this payment already exists before creating
            if (summaryRepository.findByPaymentId(paymentId).isEmpty()) {
                PaymentCustomerSummaryEntity summary = new PaymentCustomerSummaryEntity(
                        null,
                        paymentId,
                        event.customerId(),
                        event.customerName(),
                        event.customerPhoneNumber(),
                        event.customerEmail()
                );
                summaryRepository.save(summary);
            }
        }
    }
}