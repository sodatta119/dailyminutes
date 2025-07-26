package com.dailyminutes.laundry.store.service;

import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreQueryServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private StoreQueryService storeQueryService;

    @Test
    void findStoreById_shouldReturnStore() {
        StoreEntity store = new StoreEntity(1L, "Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        assertThat(storeQueryService.findStoreById(1L)).isPresent();
    }
}