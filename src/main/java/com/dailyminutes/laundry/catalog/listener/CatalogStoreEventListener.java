/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.catalog.listener;

import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogStoreOfferingSummaryEntity;
import com.dailyminutes.laundry.catalog.repository.CatalogRepository;
import com.dailyminutes.laundry.catalog.repository.CatalogStoreOfferingSummaryRepository;
import com.dailyminutes.laundry.store.domain.event.CatalogItemAddedToStoreEvent;
import com.dailyminutes.laundry.store.domain.event.StoreDeletedEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoRequestEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CatalogStoreEventListener {

    private final CatalogStoreOfferingSummaryRepository summaryRepository;
    private final CatalogRepository catalogRepository;
    private final ApplicationEventPublisher events;

    /**
     * Step 1: Reacts to an item being added to a store and requests store details.
     */
    @ApplicationModuleListener
    public void onCatalogItemAddedToStore(CatalogItemAddedToStoreEvent event) {
        // Publish an event to ask for the store's details, using the catalogId as a correlation ID.
        events.publishEvent(new StoreInfoRequestEvent(event.storeId(), event));
    }

    /**
     * Step 2: Reacts to the response from the store module and populates the summary.
     */
    @ApplicationModuleListener
    public void onStoreInfoProvided(StoreInfoResponseEvent event) {
        if (event.originalEvent() instanceof CatalogItemAddedToStoreEvent) {
            CatalogItemAddedToStoreEvent originalEvent = (CatalogItemAddedToStoreEvent) event.originalEvent();

            Long catalogId = originalEvent.catalogId(); // The store module passed the catalogId back for us.
            Optional<CatalogEntity> catalogOpt = catalogRepository.findById(catalogId);

            catalogOpt.ifPresent(catalog -> {
                // We don't have all the details from the original event here. This reveals a flaw.
                // The original event must also be passed along or its data stored temporarily.
                // For simplicity, let's assume default values for price/dates for now.
                // A more robust solution would use a temporary cache or database table for pending summaries.
                CatalogStoreOfferingSummaryEntity summary = new CatalogStoreOfferingSummaryEntity(
                        null,
                        catalog.getId(),
                        catalog.getName(),
                        catalog.getType().name(),
                        catalog.getUnitType().name(),
                        catalog.getUnitPrice(),
                        event.storeId(),
                        event.storeName(),
                        originalEvent.storeSpecificPrice(), // We lost the storeSpecificPrice
                        originalEvent.effectiveFrom(), // We lost effectiveFrom
                        originalEvent.effectiveTo(), // We lost effectiveTo
                        originalEvent.active()  // We lost the active flag
                );
                summaryRepository.save(summary);
            });
        }
    }

    @ApplicationModuleListener
    public void onStoreDeleted(StoreDeletedEvent event) {
        var summariesToDelete = summaryRepository.findByStoreId(event.storeId());
        summaryRepository.deleteAll(summariesToDelete);
    }
}