package com.dailyminutes.laundry.order.listener;

import com.dailyminutes.laundry.invoice.domain.event.InvoiceCreatedEvent;
import com.dailyminutes.laundry.invoice.domain.event.InvoiceDeletedEvent;
import com.dailyminutes.laundry.order.domain.model.OrderInvoiceSummaryEntity;
import com.dailyminutes.laundry.order.repository.OrderInvoiceSummaryRepository;
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

/**
 * The type Order invoice event listener test.
 */
@ExtendWith(MockitoExtension.class)
class OrderInvoiceEventListenerTest {

    @Mock
    private OrderInvoiceSummaryRepository summaryRepository;

    @InjectMocks
    private OrderInvoiceEventListener listener;

    /**
     * On invoice created should create summary.
     */
    @Test
    void onInvoiceCreated_shouldCreateSummary() {
        // Given: An invoice is created for order 101L
        // Assuming the event is enriched with tax and discount
        InvoiceCreatedEvent event = new InvoiceCreatedEvent(
                501L, 201L, 101L, LocalDateTime.now(),
                new BigDecimal("150.00"), new BigDecimal("15.00"), new BigDecimal("5.00")
        );
        ArgumentCaptor<OrderInvoiceSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(OrderInvoiceSummaryEntity.class);

        // When: The listener handles the event
        listener.onInvoiceCreated(event);

        // Then: A new summary entity should be saved with the correct details
        verify(summaryRepository).save(summaryCaptor.capture());
        OrderInvoiceSummaryEntity savedSummary = summaryCaptor.getValue();

        assertThat(savedSummary.getOrderId()).isEqualTo(101L);
        assertThat(savedSummary.getInvoiceId()).isEqualTo(501L);
        assertThat(savedSummary.getTotalPrice()).isEqualTo(new BigDecimal("150.00"));
        assertThat(savedSummary.getTotalTax()).isEqualTo(new BigDecimal("15.00"));
        assertThat(savedSummary.getTotalDiscount()).isEqualTo(new BigDecimal("5.00"));
        assertThat(savedSummary.getInvoiceDate()).isEqualTo(event.invoiceDate());
    }

    /**
     * On invoice deleted should delete summary.
     */
    @Test
    void onInvoiceDeleted_shouldDeleteSummary() {
        // Given: An invoice deletion event
        InvoiceDeletedEvent event = new InvoiceDeletedEvent(501L);
        OrderInvoiceSummaryEntity summaryToDelete = new OrderInvoiceSummaryEntity(
                1L, 101L, 501L, LocalDateTime.now(), new BigDecimal("150.00"),
                new BigDecimal("15.00"), new BigDecimal("5.00")
        );
        when(summaryRepository.findByInvoiceId(501L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the deletion event
        listener.onInvoiceDeleted(event);

        // Then: The repository is called to delete the specific summary record by its primary key
        verify(summaryRepository).findByInvoiceId(501L);
        verify(summaryRepository).deleteById(1L);
    }
}