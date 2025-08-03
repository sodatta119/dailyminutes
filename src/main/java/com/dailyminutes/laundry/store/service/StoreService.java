/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.service;

import com.dailyminutes.laundry.store.domain.event.*;
import com.dailyminutes.laundry.store.domain.model.StoreCatalogEntity;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.dto.CreateStoreRequest;
import com.dailyminutes.laundry.store.dto.StoreResponse;
import com.dailyminutes.laundry.store.dto.UpdateStoreRequest;
import com.dailyminutes.laundry.store.repository.StoreCatalogRepository;
import com.dailyminutes.laundry.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreCatalogRepository storeCatalogRepository;
    private final ApplicationEventPublisher events;

    public StoreResponse createStore(CreateStoreRequest request) {
        StoreEntity store = new StoreEntity(null, request.name(), request.address(), request.contactNumber(), request.email(), request.managerId());
        StoreEntity savedStore = storeRepository.save(store);
        events.publishEvent(new StoreCreatedEvent(savedStore.getId(), savedStore.getName(), savedStore.getManagerId()));
        return toStoreResponse(savedStore);
    }

    public StoreResponse updateStore(UpdateStoreRequest request) {
        StoreEntity existingStore = storeRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Store with ID " + request.id() + " not found."));

        existingStore.setName(request.name());
        existingStore.setAddress(request.address());
        existingStore.setContactNumber(request.contactNumber());
        existingStore.setEmail(request.email());
        existingStore.setManagerId(request.managerId());

        StoreEntity updatedStore = storeRepository.save(existingStore);
        events.publishEvent(new StoreUpdatedEvent(updatedStore.getId(), updatedStore.getName(), updatedStore.getManagerId()));
        return toStoreResponse(updatedStore);
    }

    public void deleteStore(Long id) {
        if (!storeRepository.existsById(id)) {
            throw new IllegalArgumentException("Store with ID " + id + " not found.");
        }
        storeRepository.deleteById(id);
        events.publishEvent(new StoreDeletedEvent(id));
    }

    public void addCatalogItemToStore(Long storeId, Long catalogId, BigDecimal price, LocalDate from, LocalDate to) {
        // Verify that both the store and catalog item exist
        if (!storeRepository.existsById(storeId)) {
            throw new IllegalArgumentException("Store with ID " + storeId + " not found.");
        }
        // NOTE: In a real scenario, you'd use a CatalogQueryService/SPI to check if the catalogId exists.
        // For now, we proceed assuming it's valid.

        StoreCatalogEntity association = new StoreCatalogEntity(null, storeId, catalogId);
        storeCatalogRepository.save(association);

        // Publish the event so the catalog module can create its summary
        events.publishEvent(new CatalogItemAddedToStoreEvent(storeId, catalogId, price, from, to, true));
    }

    public void addGeofenceToStore(Long storeId, Long geofenceId) {
        if (!storeRepository.existsById(storeId)) {
            throw new IllegalArgumentException("Store with ID " + storeId + " not found.");
        }
        // In a real application, you would also save this association
        // in a table within the 'store' module, e.g., DL_STORE_GEOFENCE.
        // For now, we will just publish the event.

        events.publishEvent(new GeofenceAssignedToStoreEvent(storeId, geofenceId));
    }

    private StoreResponse toStoreResponse(StoreEntity entity) {
        return new StoreResponse(entity.getId(), entity.getName(), entity.getAddress(), entity.getContactNumber(), entity.getEmail(), entity.getManagerId());
    }
}
