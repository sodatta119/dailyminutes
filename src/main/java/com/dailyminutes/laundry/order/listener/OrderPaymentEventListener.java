/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/08/25
 */
package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.order.domain.model.OrderPaymentSummaryEntity;
import com.dailyminutes.laundry.order.repository.OrderPaymentSummaryRepository;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPaymentEventListener {

    private final OrderPaymentSummaryRepository summaryRepository;

    @ApplicationModuleListener
    public void onPaymentMade(PaymentMadeEvent event) {
        // The event has all the data we need
        OrderPaymentSummaryEntity summary = new OrderPaymentSummaryEntity(
                null,
                event.orderId(),
                event.paymentId(),
                event.paymentDateTime(),
                event.amount(),
                "COMPLETED",
                event.method(),
                event.transactionId()
        );
        summaryRepository.save(summary);
    }
    // Add handlers for PaymentRefundedEvent, etc. to update status
}