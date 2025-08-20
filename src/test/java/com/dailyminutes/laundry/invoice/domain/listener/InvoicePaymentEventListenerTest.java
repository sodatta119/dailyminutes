package com.dailyminutes.laundry.invoice.domain.listener;

import com.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import com.dailyminutes.laundry.invoice.domain.model.InvoicePaymentSummaryEntity;
import com.dailyminutes.laundry.invoice.listener.InvoicePaymentEventListener;
import com.dailyminutes.laundry.invoice.repository.InvoicePaymentSummaryRepository;
import com.dailyminutes.laundry.invoice.repository.InvoiceRepository;
import com.dailyminutes.laundry.payment.domain.event.PaymentFailedEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentRefundedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * The type Invoice payment event listener test.
 */
@ExtendWith(MockitoExtension.class)
class InvoicePaymentEventListenerTest {

    @Mock
    private InvoicePaymentSummaryRepository paymentSummaryRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoicePaymentEventListener listener;

    /**
     * On payment made should create payment summary.
     */
    @Test
    void onPaymentMade_shouldCreatePaymentSummary() {
        // Given: A payment was made for order 1001L
        PaymentMadeEvent event = new PaymentMadeEvent(
                901L, 1001L, 201L, new BigDecimal("150.00"),
                "CREDIT_CARD", "txn_123", LocalDateTime.now()
        );

        // And an invoice exists for that order
        InvoiceEntity invoice = new InvoiceEntity(501L, "swipe-1", 1001L, 201L, LocalDateTime.now(), null, null, null);
        when(invoiceRepository.findByOrderId(1001L)).thenReturn(Optional.of(invoice));

        ArgumentCaptor<InvoicePaymentSummaryEntity> captor = ArgumentCaptor.forClass(InvoicePaymentSummaryEntity.class);

        // When: The listener handles the event
        listener.onPaymentMade(event);

        // Then: A new payment summary should be saved
        verify(paymentSummaryRepository).save(captor.capture());
        InvoicePaymentSummaryEntity savedSummary = captor.getValue();

        // And it should be linked to the correct invoice
        assertThat(savedSummary.getInvoiceId()).isEqualTo(501L);
        assertThat(savedSummary.getPaymentId()).isEqualTo(901L);
        assertThat(savedSummary.getStatus()).isEqualTo("COMPLETED");
        assertThat(savedSummary.getTransactionId()).isEqualTo("txn_123");
    }

    /**
     * On payment made should do nothing if invoice not found.
     */
    @Test
    void onPaymentMade_shouldDoNothingIfInvoiceNotFound() {
        // Given: A payment was made for an order that has no corresponding invoice
        PaymentMadeEvent event = new PaymentMadeEvent(
                901L, 1001L, 201L, new BigDecimal("150.00"),
                "CREDIT_CARD", "txn_123", LocalDateTime.now()
        );
        when(invoiceRepository.findByOrderId(1001L)).thenReturn(Optional.empty());

        // When: The listener handles the event
        listener.onPaymentMade(event);

        // Then: No summary should be created
        verify(paymentSummaryRepository, never()).save(any());
    }

    /**
     * On payment refunded should update summary status.
     */
    @Test
    void onPaymentRefunded_shouldUpdateSummaryStatus() {
        // Given: A payment is refunded
        PaymentRefundedEvent event = new PaymentRefundedEvent(901L, 1001L, new BigDecimal("150.00"));

        // And a payment summary already exists for that payment
        InvoicePaymentSummaryEntity existingSummary = new InvoicePaymentSummaryEntity(
                1L, 501L, 901L, LocalDateTime.now(), new BigDecimal("150.00"),
                "COMPLETED", "CREDIT_CARD", "txn_123"
        );
        when(paymentSummaryRepository.findByPaymentId(901L)).thenReturn(Optional.of(existingSummary));

        ArgumentCaptor<InvoicePaymentSummaryEntity> captor = ArgumentCaptor.forClass(InvoicePaymentSummaryEntity.class);

        // When: The listener handles the refund event
        listener.onPaymentRefunded(event);

        // Then: The existing summary's status is updated to REFUNDED
        verify(paymentSummaryRepository).save(captor.capture());
        InvoicePaymentSummaryEntity updatedSummary = captor.getValue();
        assertThat(updatedSummary.getId()).isEqualTo(1L);
        assertThat(updatedSummary.getStatus()).isEqualTo("REFUNDED");
    }

    /**
     * On payment failed should update summary status.
     */
    @Test
    void onPaymentFailed_shouldUpdateSummaryStatus() {
        // Given: A payment fails
        PaymentFailedEvent event = new PaymentFailedEvent(901L, 1001L, "Insufficient funds");

        // And a payment summary already exists for that payment
        InvoicePaymentSummaryEntity existingSummary = new InvoicePaymentSummaryEntity(
                1L, 501L, 901L, LocalDateTime.now(), new BigDecimal("150.00"),
                "PENDING", "CREDIT_CARD", "txn_123"
        );
        when(paymentSummaryRepository.findByPaymentId(901L)).thenReturn(Optional.of(existingSummary));

        ArgumentCaptor<InvoicePaymentSummaryEntity> captor = ArgumentCaptor.forClass(InvoicePaymentSummaryEntity.class);

        // When: The listener handles the failed event
        listener.onPaymentFailed(event);

        // Then: The existing summary's status is updated to FAILED
        verify(paymentSummaryRepository).save(captor.capture());
        InvoicePaymentSummaryEntity updatedSummary = captor.getValue();
        assertThat(updatedSummary.getId()).isEqualTo(1L);
        assertThat(updatedSummary.getStatus()).isEqualTo("FAILED");
    }
}