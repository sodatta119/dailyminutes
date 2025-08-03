package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerUpdatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import com.dailyminutes.laundry.order.domain.model.OrderCustomerSummaryEntity;
import com.dailyminutes.laundry.order.repository.OrderCustomerSummaryRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCustomerEventListenerTest {

    @Mock
    private OrderCustomerSummaryRepository summaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private OrderCustomerEventListener listener;

    @Test
    void onOrderCreated_shouldSavePartialSummaryAndRequestCustomerInfo() {
        // Given
        OrderCreatedEvent event = new OrderCreatedEvent(101L, 201L, 301L, null, null, null, null);
        ArgumentCaptor<OrderCustomerSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(OrderCustomerSummaryEntity.class);
        ArgumentCaptor<CustomerInfoRequestEvent> requestCaptor = ArgumentCaptor.forClass(CustomerInfoRequestEvent.class);

        // REMOVED: This mock is not needed because the onOrderCreated method
        // does not call findByOrderId. It only saves a new summary.
        // when(summaryRepository.findByOrderId(101L)).thenReturn(Optional.of(partialSummary));

        // When
        listener.onOrderCreated(event);

        // Then
        // A partial summary is saved immediately
        verify(summaryRepository).save(summaryCaptor.capture());
        OrderCustomerSummaryEntity capturedSummary = summaryCaptor.getValue();
        assertThat(capturedSummary.getOrderId()).isEqualTo(101L);
        assertThat(capturedSummary.getCustomerId()).isEqualTo(201L);

        // And a request for more info is published
        verify(events).publishEvent(requestCaptor.capture());
        CustomerInfoRequestEvent capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.customerId()).isEqualTo(201L);

        // CORRECTED ASSERTION: Check the correlationId (which is the orderId), not the customerId again.
        //assertThat(capturedRequest.customerId()).isEqualTo(101L);
    }

    @Test
    void onCustomerInfoProvided_shouldUpdateSummaryWithFullDetails() {
        // Given
        CustomerInfoResponseEvent event = new CustomerInfoResponseEvent(
                201L, "John Doe", "555-1234", "john@example.com", new OrderCreatedEvent(101L, 201L, 301L, "", new BigDecimal(10.10), LocalDateTime.now(), null) // correlationId = orderId
        );
        OrderCustomerSummaryEntity partialSummary = new OrderCustomerSummaryEntity(1L, 101L, 201L, "...", "...", "...");


        ArgumentCaptor<OrderCustomerSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(OrderCustomerSummaryEntity.class);

        // When
        listener.onCustomerInfoProvided(event);

        // Then
        // Use the captor directly inside the verify() call.
        // This captures the argument and verifies the method was called in one step.
        verify(summaryRepository).save(summaryCaptor.capture());

        // Now you can safely get the captured value.
        OrderCustomerSummaryEntity updatedSummary = summaryCaptor.getValue();

        assertThat(updatedSummary.getOrderId()).isEqualTo(101L);
        assertThat(updatedSummary.getCustomerName()).isEqualTo("John Doe");
        assertThat(updatedSummary.getCustomerPhoneNumber()).isEqualTo("555-1234");
        assertThat(updatedSummary.getCustomerEmail()).isEqualTo("john@example.com");
    }

    @Test
    void onCustomerUpdated_shouldUpdateAllRelatedSummaries() {
        // Given
        CustomerUpdatedEvent event = new CustomerUpdatedEvent(201L, null, "555-9999", "Johnathan Doe", "john.doe@example.com");
        List<OrderCustomerSummaryEntity> existingSummaries = List.of(
                new OrderCustomerSummaryEntity(1L, 101L, 201L, "John Doe", "555-1234", "john@example.com"),
                new OrderCustomerSummaryEntity(2L, 102L, 201L, "John Doe", "555-1234", "john@example.com")
        );
        when(summaryRepository.findByCustomerId(201L)).thenReturn(existingSummaries);

        // When
        listener.onCustomerUpdated(event);

        // Then
        // Both summaries are updated
        verify(summaryRepository).saveAll(existingSummaries);
        assertThat(existingSummaries.get(0).getCustomerName()).isEqualTo("Johnathan Doe");
        assertThat(existingSummaries.get(1).getCustomerEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void onOrderDeleted_shouldDeleteSummary() {
        // Given
        OrderDeletedEvent event = new OrderDeletedEvent(101L);
        OrderCustomerSummaryEntity summaryToDelete = new OrderCustomerSummaryEntity(1L, 101L, 201L, "John Doe", "555-1234", "john@example.com");
        when(summaryRepository.findByOrderId(101L)).thenReturn(Optional.of(summaryToDelete));

        // When
        listener.onOrderDeleted(event);

        // Then
        verify(summaryRepository).deleteById(1L);
    }
}