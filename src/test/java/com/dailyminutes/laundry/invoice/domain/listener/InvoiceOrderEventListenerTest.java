package com.dailyminutes.laundry.invoice.domain.listener;

import com.dailyminutes.laundry.invoice.domain.event.InvoiceCreatedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceDeletedEvent;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceOrderSummaryEntity;
import com.dailyminutes.laundry.invoice.repository.InvoiceOrderSummaryRepository;
import com.dailyminutes.laundry.order.domain.event.OrderInfoRequestEvent;
import com.dailyminutes.laundry.order.domain.event.OrderInfoResponseEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceOrderEventListenerTest {

    @Mock
    private InvoiceOrderSummaryRepository summaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private InvoiceOrderEventListener listener;

    @Test
    void onInvoiceCreated_shouldRequestOrderInfo() {
        // Given: An event for a newly created invoice
        InvoiceCreatedEvent event = new InvoiceCreatedEvent(
                501L, 101L, 1001L, LocalDateTime.now(), new BigDecimal("150.00")
        );
        ArgumentCaptor<OrderInfoRequestEvent> requestCaptor = ArgumentCaptor.forClass(OrderInfoRequestEvent.class);

        // When: The listener handles the invoice creation event
        listener.onInvoiceCreated(event);

        // Then: It should publish a new event to request the order's details
        verify(events).publishEvent(requestCaptor.capture());
        OrderInfoRequestEvent request = requestCaptor.getValue();

        // The request should use the invoiceId as the correlationId
        assertThat(request.orderId()).isEqualTo(1001L);
        InvoiceCreatedEvent invoiceEvent= (InvoiceCreatedEvent) request.originalEvent();
        assertThat(invoiceEvent.invoiceId()).isEqualTo(501L);
    }

    @Test
    void onOrderInfoProvided_shouldCreateOrderSummary() {
        // Given: A response event from the order module with the order's details
        OrderInfoResponseEvent event = new OrderInfoResponseEvent(
                1001L,1002L, LocalDateTime.now(), "DELIVERED",
                new BigDecimal("150.00"), new InvoiceCreatedEvent(501L, 10L, 20L, LocalDateTime.now(), new BigDecimal(100)) // correlationId is the invoiceId
        );
        ArgumentCaptor<InvoiceOrderSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(InvoiceOrderSummaryEntity.class);

        // When: The listener handles the order info response
        listener.onOrderInfoProvided(event);

        // Then: A new summary entity should be saved with all the correct details
        verify(summaryRepository).save(summaryCaptor.capture());
        InvoiceOrderSummaryEntity summary = summaryCaptor.getValue();

        assertThat(summary.getInvoiceId()).isEqualTo(501L);
        assertThat(summary.getOrderId()).isEqualTo(1001L);
        assertThat(summary.getStatus()).isEqualTo("DELIVERED");
        assertThat(summary.getTotalAmount()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    void onInvoiceDeleted_shouldDeleteSummary() {
        // Given: An invoice deletion event
        InvoiceDeletedEvent event = new InvoiceDeletedEvent(501L);
        InvoiceOrderSummaryEntity summaryToDelete = new InvoiceOrderSummaryEntity(
                1L, 501L, 1001L, LocalDateTime.now(), "DELIVERED", new BigDecimal("150.00")
        );
        when(summaryRepository.findByInvoiceId(501L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the deletion event
        listener.onInvoiceDeleted(event);

        // Then: The repository is called to delete the specific summary record
        verify(summaryRepository).findByInvoiceId(501L);
        verify(summaryRepository).deleteById(1L);
    }
}