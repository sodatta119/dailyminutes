package com.dailyminutes.laundry.store.listener;


import com.dailyminutes.laundry.store.domain.event.CatalogItemAddedToStoreEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoRequestEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoResponseEvent;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.repository.StoreRepository;
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
class StoreEventListenerTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private StoreEventListener listener;

    @Test
    void onStoreInfoRequested_shouldFindStoreAndPublishInfo() {
        // Given: A request for store details, containing the original event as payload
        CatalogItemAddedToStoreEvent originalEvent = new CatalogItemAddedToStoreEvent(
                10L, 100L, new BigDecimal("12.50"),
                LocalDate.now(), LocalDate.now().plusYears(1), true
        );
        StoreInfoRequestEvent requestEvent = new StoreInfoRequestEvent(10L, originalEvent);

        // And a store exists in the database
        StoreEntity storeEntity = new StoreEntity(10L, "Downtown Store", "123 Main St", "555-1234", "downtown@example.com", 1L);
        when(storeRepository.findById(10L)).thenReturn(Optional.of(storeEntity));

        ArgumentCaptor<StoreInfoResponseEvent> eventCaptor = ArgumentCaptor.forClass(StoreInfoResponseEvent.class);

        // When: The listener handles the request
        listener.onStoreInfoRequested(requestEvent);

        // Then: It should publish a response event with the store details
        verify(events).publishEvent(eventCaptor.capture());
        StoreInfoResponseEvent responseEvent = eventCaptor.getValue();

        // And the response event should contain the correct store info
        assertThat(responseEvent.storeId()).isEqualTo(10L);
        assertThat(responseEvent.storeName()).isEqualTo("Downtown Store");

        // And the original event payload must be passed through for correlation
        assertThat(responseEvent.originalEvent()).isEqualTo(originalEvent);
        //assertThat(responseEvent.originalEvent().catalogId()).isEqualTo(100L);
    }
}