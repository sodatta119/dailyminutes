package com.dailyminutes.laundry.invoice.domain.listener;

import com.dailyminutes.laundry.invoice.domain.model.InvoiceOrderSummaryEntity;
import com.dailyminutes.laundry.invoice.domain.model.InvoicePaymentSummaryEntity;
import com.dailyminutes.laundry.invoice.repository.InvoiceOrderSummaryRepository;
import com.dailyminutes.laundry.invoice.repository.InvoicePaymentSummaryRepository;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentRefundedEvent;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoicePaymentEventListenerTest {

    @Mock
    private InvoicePaymentSummaryRepository paymentSummaryRepository;

    @Mock
    private InvoiceOrderSummaryRepository orderSummaryRepository;

    @InjectMocks
    private InvoicePaymentEventListener listener;

    @Test
    void onPaymentMade_shouldCreatePaymentSummary() {
        // Given: A payment was made for order 1001L
        PaymentMadeEvent event = new PaymentMadeEvent(
                901L, 1001L, 201L, new BigDecimal("150.00"),
                PaymentMethod.CREDIT_CARD.name(), "txn_123", LocalDateTime.now()
        );

        // And an order summary exists linking order 1001L to invoice 501L
        InvoiceOrderSummaryEntity orderSummary = new InvoiceOrderSummaryEntity(
                1L, 501L, 1001L, LocalDateTime.now(), "DELIVERED", new BigDecimal("150.00")
        );
        when(orderSummaryRepository.findByOrderId(1001L)).thenReturn(Optional.of(orderSummary));

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
}