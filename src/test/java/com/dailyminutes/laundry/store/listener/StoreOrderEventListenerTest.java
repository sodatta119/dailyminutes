package com.dailyminutes.laundry.store.listener;

import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderUpdatedEvent;
import com.dailyminutes.laundry.store.domain.model.StoreOrderSummaryEntity;
import com.dailyminutes.laundry.store.repository.StoreOrderSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreOrderEventListenerTest {

    @Mock
    private StoreOrderSummaryRepository summaryRepository;

    @InjectMocks
    private StoreOrderEventListener listener;

    @Test
    void onOrderCreated_shouldCreateSummary() {
        // Given: An order creation event
        OrderCreatedEvent event = new OrderCreatedEvent(
                101L, 201L, 301L, "PENDING",
                new BigDecimal("99.50"), LocalDateTime.now(), Collections.emptyList()
        );
        ArgumentCaptor<StoreOrderSummaryEntity> captor = ArgumentCaptor.forClass(StoreOrderSummaryEntity.class);

        // When: The listener handles the event
        listener.onOrderCreated(event);

        // Then: A new summary entity is saved with the correct details
        verify(summaryRepository).save(captor.capture());
        StoreOrderSummaryEntity savedSummary = captor.getValue();

        assertThat(savedSummary.getStoreId()).isEqualTo(301L);
        assertThat(savedSummary.getOrderId()).isEqualTo(101L);
        assertThat(savedSummary.getCustomerId()).isEqualTo(201L);
        assertThat(savedSummary.getStatus()).isEqualTo("PENDING");
        assertThat(savedSummary.getTotalAmount()).isEqualTo(new BigDecimal("99.50"));
    }

    @Test
    void onOrderUpdated_shouldUpdateSummary() {
        // Given: An order update event
        OrderUpdatedEvent event = new OrderUpdatedEvent(
                101L, 201L, 301L, LocalDateTime.now(),
                "DELIVERED", new BigDecimal("105.00")
        );

        // And an existing summary for that order
        StoreOrderSummaryEntity existingSummary = new StoreOrderSummaryEntity(
                1L, 301L, 101L, LocalDateTime.now().minusDays(1),
                "PENDING", new BigDecimal("99.50"), 201L
        );
        when(summaryRepository.findByOrderId(101L)).thenReturn(Optional.of(existingSummary));
        ArgumentCaptor<StoreOrderSummaryEntity> captor = ArgumentCaptor.forClass(StoreOrderSummaryEntity.class);

        // When: The listener handles the event
        listener.onOrderUpdated(event);

        // Then: The existing summary is updated and saved
        verify(summaryRepository).save(captor.capture());
        StoreOrderSummaryEntity updatedSummary = captor.getValue();

        assertThat(updatedSummary.getId()).isEqualTo(1L);
        assertThat(updatedSummary.getStatus()).isEqualTo("DELIVERED");
        assertThat(updatedSummary.getTotalAmount()).isEqualTo(new BigDecimal("105.00"));
    }

    @Test
    void onOrderDeleted_shouldDeleteSummary() {
        // Given: An order deletion event
        OrderDeletedEvent event = new OrderDeletedEvent(101L);
        StoreOrderSummaryEntity summaryToDelete = new StoreOrderSummaryEntity(
                1L, 301L, 101L, LocalDateTime.now(),
                "DELIVERED", new BigDecimal("105.00"), 201L
        );
        when(summaryRepository.findByOrderId(101L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the deletion event
        listener.onOrderDeleted(event);

        // Then: The repository is called to delete the summary by its primary key
        verify(summaryRepository).findByOrderId(101L);
        verify(summaryRepository).deleteById(1L);
    }
}