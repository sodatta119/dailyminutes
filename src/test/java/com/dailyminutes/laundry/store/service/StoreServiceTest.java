package com.dailyminutes.laundry.store.service;

import com.dailyminutes.laundry.store.domain.event.StoreCreatedEvent;
import com.dailyminutes.laundry.store.domain.event.StoreDeletedEvent;
import com.dailyminutes.laundry.store.domain.event.StoreUpdatedEvent;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.dto.CreateStoreRequest;
import com.dailyminutes.laundry.store.dto.UpdateStoreRequest;
import com.dailyminutes.laundry.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private StoreService storeService;

    @Test
    void createStore_shouldCreateAndPublishEvent() {
        CreateStoreRequest request = new CreateStoreRequest("Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        StoreEntity store = new StoreEntity(1L, "Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        when(storeRepository.save(any())).thenReturn(store);

        storeService.createStore(request);

        verify(events).publishEvent(any(StoreCreatedEvent.class));
    }

    @Test
    void updateStore_shouldUpdateAndPublishEvent() {
        UpdateStoreRequest request = new UpdateStoreRequest(1L, "Updated Store", "456 Main St", "0987654321", "updated@test.com", 2L);
        StoreEntity store = new StoreEntity(1L, "Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeRepository.save(any())).thenReturn(store);

        storeService.updateStore(request);

        verify(events).publishEvent(any(StoreUpdatedEvent.class));
    }

    @Test
    void deleteStore_shouldDeleteAndPublishEvent() {
        when(storeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(storeRepository).deleteById(1L);

        storeService.deleteStore(1L);

        verify(events).publishEvent(any(StoreDeletedEvent.class));
    }

    @Test
    void updateStore_shouldThrowException_whenStoreNotFound() {
        UpdateStoreRequest request = new UpdateStoreRequest(1L, "Updated Store", "456 Main St", "0987654321", "updated@test.com", 2L);
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> storeService.updateStore(request));
    }
}
