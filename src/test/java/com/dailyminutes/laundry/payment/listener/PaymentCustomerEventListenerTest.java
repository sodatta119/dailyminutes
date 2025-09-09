package com.dailyminutes.laundry.payment.listener;

import com.dailyminutes.laundry.common.events.CallerEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerDeletedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentDeletedEvent;
import com.dailyminutes.laundry.payment.domain.event.PaymentMadeEvent;
import com.dailyminutes.laundry.payment.domain.model.PaymentCustomerSummaryEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.repository.PaymentCustomerSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * The type Payment customer event listener test.
 */
@ExtendWith(MockitoExtension.class)
class PaymentCustomerEventListenerTest {

    @Mock
    private PaymentCustomerSummaryRepository summaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private PaymentCustomerEventListener listener;

    /**
     * On payment made should request customer info.
     */
    @Test
    void onPaymentMade_shouldRequestCustomerInfo() {
        // Given: a payment is made
        PaymentMadeEvent event = new PaymentMadeEvent(1L, 101L, 201L, BigDecimal.TEN, PaymentMethod.CASH.name(), "txn123",LocalDateTime.now());
        ArgumentCaptor<CustomerInfoRequestEvent> captor = ArgumentCaptor.forClass(CustomerInfoRequestEvent.class);

        // When: the listener handles the event
        listener.onPaymentMade(event);

        // Then: it should publish a request for that customer's details
        verify(events).publishEvent(captor.capture());
        CustomerInfoRequestEvent requestedEvent = captor.getValue();
        assertThat(requestedEvent.customerId()).isEqualTo(201L);
        assertThat(requestedEvent.originalEvent()).isEqualTo(event);
    }

    /**
     * On customer info provided should create summary when original event is payment made.
     */
    @Test
    void onCustomerInfoProvided_shouldCreateSummary_whenOriginalEventIsPaymentMade() {
        // Given: the customer module provides info, and the original event was for a payment
        PaymentMadeEvent originalEvent = new PaymentMadeEvent(1L, 101L, 201L, BigDecimal.TEN, PaymentMethod.CASH.name(), "txn123", LocalDateTime.now());
        CustomerInfoResponseEvent event = new CustomerInfoResponseEvent(201L, "John Doe", "555-1234", "john@test.com", "test-address", "000","000",100L, originalEvent);
        ArgumentCaptor<PaymentCustomerSummaryEntity> captor = ArgumentCaptor.forClass(PaymentCustomerSummaryEntity.class);

        // When: the listener handles the response
        listener.onCustomerInfoProvided(event);

        // Then: a new summary should be saved with all the correct data
        verify(summaryRepository).save(captor.capture());
        PaymentCustomerSummaryEntity summary = captor.getValue();
        assertThat(summary.getPaymentId()).isEqualTo(1L);
        assertThat(summary.getCustomerId()).isEqualTo(201L);
        assertThat(summary.getCustomerName()).isEqualTo("John Doe");
    }

    /**
     * On customer info provided should do nothing when original event is not payment made.
     */
    @Test
    void onCustomerInfoProvided_shouldDoNothing_whenOriginalEventIsNotPaymentMade() {
        // Given: the customer module provides info, but the original event was something else
        CallerEvent otherEvent = mock(CallerEvent.class);
        CustomerInfoResponseEvent event = new CustomerInfoResponseEvent(201L, "John Doe", "555-1234", "john@test.com", "test-address", "000","000",100L, otherEvent);

        // When: the listener handles the response
        listener.onCustomerInfoProvided(event);

        // Then: no summary should be saved because the 'instanceof' check fails
        verify(summaryRepository, never()).save(any());
    }

    /**
     * On payment deleted should delete summary.
     */
    @Test
    void onPaymentDeleted_shouldDeleteSummary() {
        // Given
        PaymentDeletedEvent event = new PaymentDeletedEvent(1L);
        PaymentCustomerSummaryEntity summary = new PaymentCustomerSummaryEntity(5L, 1L, 201L, "John", "555", "email");
        when(summaryRepository.findByPaymentId(1L)).thenReturn(Optional.of(summary));

        // When
        listener.onPaymentDeleted(event);

        // Then
        verify(summaryRepository).deleteById(5L);
    }

    /**
     * On customer deleted should delete all summaries for customer.
     */
    @Test
    void onCustomerDeleted_shouldDeleteAllSummariesForCustomer() {
        // Given
        CustomerDeletedEvent event = new CustomerDeletedEvent(201L);
        List<PaymentCustomerSummaryEntity> summaries = List.of(new PaymentCustomerSummaryEntity());
        when(summaryRepository.findByCustomerId(201L)).thenReturn(summaries);

        // When
        listener.onCustomerDeleted(event);

        // Then
        verify(summaryRepository).deleteAll(summaries);
    }
}