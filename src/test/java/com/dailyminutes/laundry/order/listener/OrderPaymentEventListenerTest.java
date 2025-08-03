package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.order.domain.model.OrderPaymentSummaryEntity;
import com.dailyminutes.laundry.order.repository.OrderPaymentSummaryRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderPaymentEventListenerTest {

    @Mock
    private OrderPaymentSummaryRepository summaryRepository;

    @InjectMocks
    private OrderPaymentEventListener listener;

    @Test
    void onPaymentMade_shouldCreateSummary() {
        // Given
        PaymentMadeEvent event = new PaymentMadeEvent(
                901L, 101L, 201L, new BigDecimal("150.00"),
                "CREDIT_CARD", "txn_123", LocalDateTime.now()
        );
        ArgumentCaptor<OrderPaymentSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(OrderPaymentSummaryEntity.class);

        // When
        listener.onPaymentMade(event);

        // Then
        verify(summaryRepository).save(summaryCaptor.capture());
        OrderPaymentSummaryEntity savedSummary = summaryCaptor.getValue();

        assertThat(savedSummary.getOrderId()).isEqualTo(101L);
        assertThat(savedSummary.getPaymentId()).isEqualTo(901L);
        assertThat(savedSummary.getStatus()).isEqualTo("COMPLETED");
        assertThat(savedSummary.getMethod()).isEqualTo("CREDIT_CARD");
        assertThat(savedSummary.getTransactionId()).isEqualTo("txn_123");
    }

    @Test
    void onPaymentRefunded_shouldUpdateSummaryStatus() {
        // Given
        PaymentRefundedEvent event = new PaymentRefundedEvent(901L, 101L, new BigDecimal("150.00"));
        OrderPaymentSummaryEntity existingSummary = new OrderPaymentSummaryEntity(
                1L, 101L, 901L, LocalDateTime.now(), new BigDecimal("150.00"),
                "COMPLETED", "CREDIT_CARD", "txn_123"
        );
        when(summaryRepository.findByPaymentId(901L)).thenReturn(Optional.of(existingSummary));
        ArgumentCaptor<OrderPaymentSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(OrderPaymentSummaryEntity.class);

        // When
        listener.onPaymentRefunded(event);

        // Then
        verify(summaryRepository).save(summaryCaptor.capture());
        OrderPaymentSummaryEntity updatedSummary = summaryCaptor.getValue();

        assertThat(updatedSummary.getId()).isEqualTo(1L);
        assertThat(updatedSummary.getStatus()).isEqualTo("REFUNDED");
    }

    @Test
    void onPaymentFailed_shouldUpdateSummaryStatus() {
        // Given
        PaymentFailedEvent event = new PaymentFailedEvent(901L, 101L, "Declined");
        OrderPaymentSummaryEntity existingSummary = new OrderPaymentSummaryEntity(
                1L, 101L, 901L, LocalDateTime.now(), new BigDecimal("150.00"),
                "PENDING", "CREDIT_CARD", "txn_123"
        );
        when(summaryRepository.findByPaymentId(901L)).thenReturn(Optional.of(existingSummary));
        ArgumentCaptor<OrderPaymentSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(OrderPaymentSummaryEntity.class);

        // When
        listener.onPaymentFailed(event);

        // Then
        verify(summaryRepository).save(summaryCaptor.capture());
        OrderPaymentSummaryEntity updatedSummary = summaryCaptor.getValue();

        assertThat(updatedSummary.getId()).isEqualTo(1L);
        assertThat(updatedSummary.getStatus()).isEqualTo("FAILED");
    }
}