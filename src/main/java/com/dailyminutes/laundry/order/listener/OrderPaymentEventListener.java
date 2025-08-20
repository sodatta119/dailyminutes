/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/08/25
 */
package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.order.domain.model.OrderPaymentSummaryEntity;
import com.dailyminutes.laundry.order.repository.OrderPaymentSummaryRepository;
import com.dailyminutes.laundry.payment.domain.event.PaymentFailedEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Order payment event listener.
 */
@Component
@RequiredArgsConstructor
public class OrderPaymentEventListener {

    private final OrderPaymentSummaryRepository summaryRepository;

    /**
     * Creates a new summary record when a payment is completed.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onPaymentMade(PaymentMadeEvent event) {
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

    /**
     * Updates the summary status when a payment is refunded.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onPaymentRefunded(PaymentRefundedEvent event) {
        summaryRepository.findByPaymentId(event.paymentId()).ifPresent(summary -> {
            summary.setStatus("REFUNDED");
            summaryRepository.save(summary);
        });
    }

    /**
     * Updates the summary status when a payment fails.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onPaymentFailed(PaymentFailedEvent event) {
        summaryRepository.findByPaymentId(event.paymentId()).ifPresent(summary -> {
            summary.setStatus("FAILED");
            summaryRepository.save(summary);
        });
    }
}