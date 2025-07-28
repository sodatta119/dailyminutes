/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.catalog.listener;

import com.dailyminutes.laundry.catalog.domain.model.CatalogOrderItemSummaryEntity;
import com.dailyminutes.laundry.catalog.repository.CatalogOrderItemSummaryRepository;
import com.dailyminutes.laundry.catalog.repository.CatalogRepository;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CatalogOrderEventListener {

    private final CatalogRepository catalogRepository;
    private final CatalogOrderItemSummaryRepository summaryRepository;

    @ApplicationModuleListener
    public void onOrderCreated(OrderCreatedEvent event) {
        // The listener now gets all required item info directly from the event
        List<CatalogOrderItemSummaryEntity> summaries = event.items().stream()
                .map(itemInfo ->
                        catalogRepository.findById(itemInfo.catalogId()).map(catalog ->
                                new CatalogOrderItemSummaryEntity(
                                        null,
                                        catalog.getId(),
                                        catalog.getName(),
                                        catalog.getType().name(),
                                        catalog.getUnitType().name(),
                                        event.orderId(),
                                        itemInfo.orderItemId(),
                                        itemInfo.quantity().intValue(),
                                        itemInfo.itemPriceAtOrder(),
                                        event.orderDate()
                                )
                        ).orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        summaryRepository.saveAll(summaries);
    }

    @ApplicationModuleListener
    public void onOrderDeleted(OrderDeletedEvent event) {
        var summariesToDelete = summaryRepository.findByOrderId(event.orderId());
        summaryRepository.deleteAll(summariesToDelete);
    }
}