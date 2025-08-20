package com.dailyminutes.laundry.store.service;

import com.dailyminutes.laundry.store.domain.event.*;
import com.dailyminutes.laundry.store.domain.model.StoreCatalogEntity;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.domain.model.StoreGeofenceSummaryEntity;
import com.dailyminutes.laundry.store.dto.CreateStoreRequest;
import com.dailyminutes.laundry.store.dto.UpdateStoreRequest;
import com.dailyminutes.laundry.store.repository.StoreCatalogRepository;
import com.dailyminutes.laundry.store.repository.StoreGeofenceSummaryRepository;
import com.dailyminutes.laundry.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The type Store service test.
 */
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private StoreCatalogRepository storeCatalogRepository;
    @Mock
    private StoreGeofenceSummaryRepository storeGeofenceSummaryRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private StoreService storeService;

    /**
     * Create store should create and publish event.
     */
    @Test
    void createStore_shouldCreateAndPublishEvent() {
        CreateStoreRequest request = new CreateStoreRequest("Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        StoreEntity store = new StoreEntity(1L, "Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        when(storeRepository.save(any())).thenReturn(store);

        storeService.createStore(request);

        verify(events).publishEvent(any(StoreCreatedEvent.class));
    }

    /**
     * Update store should update and publish event.
     */
    @Test
    void updateStore_shouldUpdateAndPublishEvent() {
        UpdateStoreRequest request = new UpdateStoreRequest(1L, "Updated Store", "456 Main St", "0987654321", "updated@test.com", 2L);
        StoreEntity store = new StoreEntity(1L, "Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeRepository.save(any())).thenReturn(store);

        storeService.updateStore(request);

        verify(events).publishEvent(any(StoreUpdatedEvent.class));
    }

    /**
     * Delete store should delete and publish event.
     */
    @Test
    void deleteStore_shouldDeleteAndPublishEvent() {
        when(storeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(storeRepository).deleteById(1L);

        storeService.deleteStore(1L);

        verify(events).publishEvent(any(StoreDeletedEvent.class));
    }

    /**
     * Update store should throw exception when store not found.
     */
    @Test
    void updateStore_shouldThrowException_whenStoreNotFound() {
        UpdateStoreRequest request = new UpdateStoreRequest(1L, "Updated Store", "456 Main St", "0987654321", "updated@test.com", 2L);
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> storeService.updateStore(request));
    }

    /**
     * Remove catalog item from store should delete and publish event.
     */
    @Test
    void removeCatalogItemFromStore_shouldDeleteAndPublishEvent() {
        // Given
        Long storeId = 1L;
        Long catalogId = 101L;
        StoreCatalogEntity association = new StoreCatalogEntity(5L, storeId, catalogId);
        when(storeCatalogRepository.findByStoreIdAndCatalogId(storeId, catalogId))
                .thenReturn(Optional.of(association));
        ArgumentCaptor<CatalogItemRemovedFromStoreEvent> eventCaptor = ArgumentCaptor.forClass(CatalogItemRemovedFromStoreEvent.class);

        // When
        storeService.removeCatalogItemFromStore(storeId, catalogId);

        // Then
        verify(storeCatalogRepository).delete(association);
        verify(events).publishEvent(eventCaptor.capture());

        CatalogItemRemovedFromStoreEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.storeId()).isEqualTo(storeId);
        assertThat(publishedEvent.catalogId()).isEqualTo(catalogId);
    }

    /**
     * Assign geofence to store should publish event.
     */
    @Test
    void assignGeofenceToStore_shouldPublishEvent() {
        // Given
        Long storeId = 1L;
        Long geofenceId = 100L;
        when(storeRepository.existsById(storeId)).thenReturn(true);
        ArgumentCaptor<GeofenceAssignedToStoreEvent> eventCaptor = ArgumentCaptor.forClass(GeofenceAssignedToStoreEvent.class);

        // When
        storeService.assignGeofenceToStore(storeId, geofenceId);

        // Then
        // It should only verify its own repository was checked...
        verify(storeRepository).existsById(storeId);
        // ...and that the event was published.
        verify(events).publishEvent(eventCaptor.capture());

        GeofenceAssignedToStoreEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.storeId()).isEqualTo(storeId);
        assertThat(publishedEvent.geofenceId()).isEqualTo(geofenceId);
    }

    /**
     * Remove geofence from store should delete and publish event.
     */
    @Test
    void removeGeofenceFromStore_shouldDeleteAndPublishEvent() {
        // Given
        Long storeId = 1L;
        Long geofenceId = 100L;
        StoreGeofenceSummaryEntity summary = new StoreGeofenceSummaryEntity(5L, storeId, geofenceId, "name", "type", true);

        // Mock the repository to return the summary
        when(storeGeofenceSummaryRepository.findByStoreId(storeId)).thenReturn(List.of(summary));
        ArgumentCaptor<GeofenceRemovedFromStoreEvent> eventCaptor = ArgumentCaptor.forClass(GeofenceRemovedFromStoreEvent.class);

        // When
        storeService.removeGeofenceFromStore(storeId, geofenceId);

        // Then
        verify(storeGeofenceSummaryRepository).delete(summary);
        verify(events).publishEvent(eventCaptor.capture());

        GeofenceRemovedFromStoreEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.storeId()).isEqualTo(storeId);
        assertThat(publishedEvent.geofenceId()).isEqualTo(geofenceId);
    }
}
