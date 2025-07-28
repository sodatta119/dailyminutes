package com.dailyminutes.laundry.catalog.listener;


import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogStoreOfferingSummaryEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;
import com.dailyminutes.laundry.catalog.repository.CatalogRepository;
import com.dailyminutes.laundry.catalog.repository.CatalogStoreOfferingSummaryRepository;
import com.dailyminutes.laundry.store.domain.event.CatalogItemAddedToStoreEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoRequestEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoResponseEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogStoreEventListenerTest {

    @Mock
    private CatalogStoreOfferingSummaryRepository summaryRepository;

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private CatalogStoreEventListener listener;

    @Test
    void onCatalogItemAddedToStore_shouldRequestStoreInfo() {
        // Given: An event indicating a catalog item has been associated with a store
        CatalogItemAddedToStoreEvent event = new CatalogItemAddedToStoreEvent(
                10L, 100L, new BigDecimal("12.50"),
                LocalDate.now(), LocalDate.now().plusYears(1), true
        );
        ArgumentCaptor<StoreInfoRequestEvent> requestCaptor = ArgumentCaptor.forClass(StoreInfoRequestEvent.class);

        // When: The listener handles this initial event
        listener.onCatalogItemAddedToStore(event);

        // Then: It should publish a new event to request more details about the store
        verify(events).publishEvent(requestCaptor.capture());
        StoreInfoRequestEvent request = requestCaptor.getValue();

        assertThat(request.storeId()).isEqualTo(10L);
        // And the original event must be passed along as the payload
        assertThat(request.originalEvent()).isEqualTo(event);
    }

    @Test
    void onStoreInfoProvided_shouldCreateSummary() {
        // Given: The response event from the store module, containing the store's name
        // and the original event payload
        CatalogItemAddedToStoreEvent originalEvent = new CatalogItemAddedToStoreEvent(
                10L, 100L, new BigDecimal("12.50"),
                LocalDate.now(), LocalDate.now().plusYears(1), true
        );
        StoreInfoResponseEvent event = new StoreInfoResponseEvent(10L, "Downtown Store", "test-address", originalEvent);

        // And the corresponding catalog item exists
        CatalogEntity catalogEntity = new CatalogEntity(100L, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("15.00"));
        when(catalogRepository.findById(100L)).thenReturn(Optional.of(catalogEntity));

        ArgumentCaptor<CatalogStoreOfferingSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(CatalogStoreOfferingSummaryEntity.class);

        // When: The listener handles the response event
        listener.onStoreInfoProvided(event);

        // Then: The final summary entity should be saved
        verify(summaryRepository).save(summaryCaptor.capture());
        CatalogStoreOfferingSummaryEntity summary = summaryCaptor.getValue();

        // And the summary should be populated with data from both the catalog and the events
        assertThat(summary.getCatalogId()).isEqualTo(100L);
        assertThat(summary.getCatalogName()).isEqualTo("Dry Cleaning");
        assertThat(summary.getStoreId()).isEqualTo(10L);
        assertThat(summary.getStoreName()).isEqualTo("Downtown Store");
        assertThat(summary.getStoreSpecificPrice()).isEqualTo(new BigDecimal("12.50"));
        assertThat(summary.isActive()).isTrue();
    }
}