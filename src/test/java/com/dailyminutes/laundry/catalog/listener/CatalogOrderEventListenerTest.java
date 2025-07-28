package com.dailyminutes.laundry.catalog.listener;

import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogOrderItemSummaryEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;
import com.dailyminutes.laundry.catalog.repository.CatalogOrderItemSummaryRepository;
import com.dailyminutes.laundry.catalog.repository.CatalogRepository;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderItemInfo;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogOrderEventListenerTest {

    @Captor
    private ArgumentCaptor<List<CatalogOrderItemSummaryEntity>> captor;

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private CatalogOrderItemSummaryRepository summaryRepository;

    @InjectMocks
    private CatalogOrderEventListener listener;

    @Test
    void onOrderCreated_shouldCreateSummariesForOrderItems() {
        // Given: An order is created with two distinct catalog items
        OrderItemInfo item1 = new OrderItemInfo(101L, 1L, new BigDecimal("2.0"), new BigDecimal("10.00"));
        OrderItemInfo item2 = new OrderItemInfo(102L, 2L, new BigDecimal("1.0"), new BigDecimal("5.50"));
        OrderCreatedEvent event = new OrderCreatedEvent(
                50L, 1L, 1L, OrderStatus.PENDING.name(), new BigDecimal("25.50"),
                LocalDateTime.now(), List.of(item1, item2)
        );

        // And the corresponding catalog entities exist
        CatalogEntity catalog1 = new CatalogEntity(1L, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("5.00"));
        CatalogEntity catalog2 = new CatalogEntity(2L, CatalogType.SERVICE, "Ironing", UnitType.PIECES, new BigDecimal("5.50"));
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(catalog1));
        when(catalogRepository.findById(2L)).thenReturn(Optional.of(catalog2));


        // When: The listener handles the order creation event
        listener.onOrderCreated(event);

        // Then: The summary repository should be called to save a list of summaries
        verify(summaryRepository).saveAll(captor.capture());
        List<CatalogOrderItemSummaryEntity> savedSummaries = captor.getValue();

        // And exactly two summaries should have been created
        assertThat(savedSummaries).hasSize(2);

        // And the summary details should match the event and catalog data
        CatalogOrderItemSummaryEntity summary1 = savedSummaries.stream().filter(s -> s.getCatalogId().equals(1L)).findFirst().get();
        assertThat(summary1.getOrderId()).isEqualTo(50L);
        assertThat(summary1.getOrderItemId()).isEqualTo(101L);
        assertThat(summary1.getCatalogName()).isEqualTo("Wash & Fold");
        assertThat(summary1.getItemPriceAtOrder()).isEqualTo(new BigDecimal("10.00"));

        CatalogOrderItemSummaryEntity summary2 = savedSummaries.stream().filter(s -> s.getCatalogId().equals(2L)).findFirst().get();
        assertThat(summary2.getOrderId()).isEqualTo(50L);
        assertThat(summary2.getOrderItemId()).isEqualTo(102L);
        assertThat(summary2.getCatalogName()).isEqualTo("Ironing");
        assertThat(summary2.getQuantity()).isEqualTo(1);
    }

    @Test
    void onOrderCreated_shouldHandleOrdersWithNoItems() {
        // Given: An order is created with an empty list of items
        OrderCreatedEvent event = new OrderCreatedEvent(
                51L, 1L, 1L, OrderStatus.PENDING.name(), BigDecimal.ZERO,
                LocalDateTime.now(), Collections.emptyList()
        );

        // When: The listener handles the event
        listener.onOrderCreated(event);

        // Then: The saveAll method should be called with an empty list
        verify(summaryRepository).saveAll(Collections.emptyList());
    }

    @Test
    void onOrderDeleted_shouldDeleteAllSummariesForThatOrder() {
        // Given: An order deletion event for order ID 55
        OrderDeletedEvent event = new OrderDeletedEvent(55L);
        List<CatalogOrderItemSummaryEntity> summariesToDelete = List.of(new CatalogOrderItemSummaryEntity(), new CatalogOrderItemSummaryEntity());
        when(summaryRepository.findByOrderId(55L)).thenReturn(summariesToDelete);

        // When: The listener handles the deletion event
        listener.onOrderDeleted(event);

        // Then: It should find the related summaries and delete them
        verify(summaryRepository).findByOrderId(55L);
        verify(summaryRepository).deleteAll(summariesToDelete);
    }
}