/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/08/25
 */
package com.dailyminutes.laundry.invoice.domain.listener;

import com.dailyminutes.laundry.invoice.domain.model.InvoicePaymentSummaryEntity;
import com.dailyminutes.laundry.invoice.repository.InvoiceOrderSummaryRepository;
import com.dailyminutes.laundry.invoice.repository.InvoicePaymentSummaryRepository;
import com.dailyminutes.laundry.payment.domain.event.PaymentFailedEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoicePaymentEventListener {

    private final InvoicePaymentSummaryRepository paymentSummaryRepository;
    private final InvoiceOrderSummaryRepository orderSummaryRepository;

    @ApplicationModuleListener
    public void onPaymentMade(PaymentMadeEvent event) {
        // Use an existing summary within this module to find the invoiceId
        orderSummaryRepository.findByOrderId(event.orderId()).ifPresent(orderSummary -> {
            InvoicePaymentSummaryEntity summary = new InvoicePaymentSummaryEntity(
                    null,
                    orderSummary.getInvoiceId(),
                    event.paymentId(),
                    event.paymentDateTime(),
                    event.amount(),
                    "COMPLETED",
                    event.method(),
                    event.transactionId()
            );
            paymentSummaryRepository.save(summary);
        });
    }

    @ApplicationModuleListener
    public void onPaymentRefunded(PaymentRefundedEvent event) {
        // Find the summary by paymentId and update its status
        paymentSummaryRepository.findByPaymentId(event.paymentId()).ifPresent(summary -> {
            summary.setStatus("REFUNDED");
            paymentSummaryRepository.save(summary);
        });
    }

    @ApplicationModuleListener
    public void onPaymentFailed(PaymentFailedEvent event) {
        // Find the summary by paymentId and update its status
        paymentSummaryRepository.findByPaymentId(event.paymentId()).ifPresent(summary -> {
            summary.setStatus("FAILED");
            paymentSummaryRepository.save(summary);
        });
    }
}