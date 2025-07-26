/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.api;

import com.dailyminutes.laundry.store.dto.*;
import com.dailyminutes.laundry.store.service.StoreQueryService;
import com.dailyminutes.laundry.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final StoreQueryService storeQueryService;

    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@Valid @RequestBody CreateStoreRequest request) {
        return new ResponseEntity<>(storeService.createStore(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> updateStore(@PathVariable Long id, @Valid @RequestBody UpdateStoreRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(storeService.updateStore(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable Long id) {
        return storeQueryService.findStoreById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        return ResponseEntity.ok(storeQueryService.findAllStores());
    }

    @GetMapping("/{id}/agent-summary")
    public ResponseEntity<List<StoreAgentSummaryResponse>> getAgentSummary(@PathVariable Long id) {
        return ResponseEntity.ok(storeQueryService.findAgentSummariesByStoreId(id));
    }

    @GetMapping("/{id}/geofence-summary")
    public ResponseEntity<List<StoreGeofenceSummaryResponse>> getGeofenceSummary(@PathVariable Long id) {
        return ResponseEntity.ok(storeQueryService.findGeofenceSummariesByStoreId(id));
    }

    @GetMapping("/{id}/order-summary")
    public ResponseEntity<List<StoreOrderSummaryResponse>> getOrderSummary(@PathVariable Long id) {
        return ResponseEntity.ok(storeQueryService.findOrderSummariesByStoreId(id));
    }

    @GetMapping("/{id}/task-summary")
    public ResponseEntity<List<StoreTaskSummaryResponse>> getTaskSummary(@PathVariable Long id) {
        return ResponseEntity.ok(storeQueryService.findTaskSummariesByStoreId(id));
    }
}
