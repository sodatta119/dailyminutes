package com.dailyminutes.laundry.invoice.domain.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerDeletedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceCreatedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceDeletedEvent;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceCustomerSummaryEntity;
import com.dailyminutes.laundry.invoice.listener.InvoiceCustomerEventListener;
import com.dailyminutes.laundry.invoice.repository.InvoiceCustomerSummaryRepository;
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
class InvoiceCustomerEventListenerTest {

    @Mock
    private InvoiceCustomerSummaryRepository summaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private InvoiceCustomerEventListener listener;

    @Test
    void onInvoiceCreated_shouldRequestCustomerInfo() {
        // Given: An event for a newly created invoice
        InvoiceCreatedEvent event = new InvoiceCreatedEvent(
                501L, 101L, 1001L, LocalDateTime.now(), new BigDecimal("150.00"), new BigDecimal("15.00"), new BigDecimal("5.00")
        );
        ArgumentCaptor<CustomerInfoRequestEvent> requestCaptor = ArgumentCaptor.forClass(CustomerInfoRequestEvent.class);

        // When: The listener handles the invoice creation event
        listener.onInvoiceCreated(event);

        // Then: It should publish a new event to request the customer's details
        verify(events).publishEvent(requestCaptor.capture());
        CustomerInfoRequestEvent request = requestCaptor.getValue();

        // The request should use the invoiceId as the correlationId
        assertThat(request.customerId()).isEqualTo(101L);
        InvoiceCreatedEvent invoiceCreatedEvent = (InvoiceCreatedEvent) request.originalEvent();
        assertThat(invoiceCreatedEvent.invoiceId()).isEqualTo(501L);
    }

    @Test
    void onCustomerInfoProvided_shouldCreateSummary() {
        // Given: A response event from the customer module with the customer's details
        CustomerInfoResponseEvent event = new CustomerInfoResponseEvent(
                101L, "Jane Doe", "555-1234", "jane.doe@example.com", new InvoiceCreatedEvent(501L, 10L, 20L, LocalDateTime.now(), new BigDecimal(10.20), new BigDecimal(1.20), new BigDecimal(0.20)) // correlationId is the invoiceId
        );
        ArgumentCaptor<InvoiceCustomerSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(InvoiceCustomerSummaryEntity.class);

        // When: The listener handles the customer info response
        listener.onCustomerInfoProvided(event);

        // Then: A new summary entity should be saved with all the correct details
        verify(summaryRepository).save(summaryCaptor.capture());
        InvoiceCustomerSummaryEntity summary = summaryCaptor.getValue();

        assertThat(summary.getInvoiceId()).isEqualTo(501L);
        assertThat(summary.getCustomerId()).isEqualTo(101L);
        assertThat(summary.getCustomerName()).isEqualTo("Jane Doe");
        assertThat(summary.getCustomerPhoneNumber()).isEqualTo("555-1234");
        assertThat(summary.getCustomerEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    void onInvoiceDeleted_shouldDeleteSummary() {
        // Given: An invoice deletion event
        InvoiceDeletedEvent event = new InvoiceDeletedEvent(501L);
        InvoiceCustomerSummaryEntity summaryToDelete = new InvoiceCustomerSummaryEntity(
                1L, 501L, 101L, "Jane Doe", "555-1234", "jane.doe@example.com"
        );
        when(summaryRepository.findByInvoiceId(501L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the deletion event
        listener.onInvoiceDeleted(event);

        // Then: The repository is called to delete the specific summary record
        verify(summaryRepository).findByInvoiceId(501L);
        verify(summaryRepository).deleteById(1L);
    }

    @Test
    void onCustomerDeleted_shouldDeleteAllSummariesForThatCustomer() {
        // Given: A customer deletion event
        CustomerDeletedEvent event = new CustomerDeletedEvent(101L);
        List<InvoiceCustomerSummaryEntity> summariesToDelete = List.of(
                new InvoiceCustomerSummaryEntity(), new InvoiceCustomerSummaryEntity()
        );
        when(summaryRepository.findByCustomerId(101L)).thenReturn(summariesToDelete);

        // When: The listener handles the deletion event
        listener.onCustomerDeleted(event);

        // Then: The repository is called to delete all summaries for that customer
        verify(summaryRepository).findByCustomerId(101L);
        verify(summaryRepository).deleteAll(summariesToDelete);
    }
}