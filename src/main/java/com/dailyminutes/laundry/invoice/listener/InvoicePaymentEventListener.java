/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/08/25
 */
package com.dailyminutes.laundry.invoice.listener;

import com.dailyminutes.laundry.invoice.domain.model.InvoicePaymentSummaryEntity;
import com.dailyminutes.laundry.invoice.repository.InvoicePaymentSummaryRepository;
import com.dailyminutes.laundry.invoice.repository.InvoiceRepository;
import com.dailyminutes.laundry.payment.domain.event.PaymentFailedEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Invoice payment event listener.
 */
@Component
@RequiredArgsConstructor
public class InvoicePaymentEventListener {

    private final InvoicePaymentSummaryRepository paymentSummaryRepository;
    private final InvoiceRepository invoiceRepository;

    /**
     * On payment made.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onPaymentMade(PaymentMadeEvent event) {
        // Use the local repository to find the invoiceId from the orderId
        invoiceRepository.findByOrderId(event.orderId()).ifPresent(invoice -> {
            InvoicePaymentSummaryEntity summary = new InvoicePaymentSummaryEntity(
                    null,
                    invoice.getId(), // Use the found invoiceId
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

    /**
     * On payment refunded.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onPaymentRefunded(PaymentRefundedEvent event) {
        paymentSummaryRepository.findByPaymentId(event.paymentId()).ifPresent(summary -> {
            summary.setStatus("REFUNDED");
            paymentSummaryRepository.save(summary);
        });
    }

    /**
     * On payment failed.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onPaymentFailed(PaymentFailedEvent event) {
        paymentSummaryRepository.findByPaymentId(event.paymentId()).ifPresent(summary -> {
            summary.setStatus("FAILED");
            paymentSummaryRepository.save(summary);
        });
    }
}