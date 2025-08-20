// FILE: src/main/java/com/dailyminutes/laundry/store/api/StoreController.java
package com.dailyminutes.laundry.store.api;

import com.dailyminutes.laundry.store.dto.*;
import com.dailyminutes.laundry.store.service.StoreQueryService;
import com.dailyminutes.laundry.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "Store Management", description = "APIs for managing stores")
public class StoreController {

    private final StoreService storeService;
    private final StoreQueryService storeQueryService;

    @Operation(summary = "Create a new store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@Valid @RequestBody CreateStoreRequest request) {
        return new ResponseEntity<>(storeService.createStore(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> updateStore(@PathVariable Long id, @Valid @RequestBody UpdateStoreRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(storeService.updateStore(request));
    }

    @Operation(summary = "Delete a store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Store deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a store by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the store"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable Long id) {
        return storeQueryService.findStoreById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));
    }

    @Operation(summary = "Get all stores")
    @ApiResponse(responseCode = "200", description = "List of all stores")
    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        return ResponseEntity.ok(storeQueryService.findAllStores());
    }

    @Operation(summary = "Get agent summaries for a store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found agent summaries"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{id}/agent-summary")
    public ResponseEntity<List<StoreAgentSummaryResponse>> getAgentSummary(@PathVariable Long id) {
        return ResponseEntity.ok(storeQueryService.findAgentSummariesByStoreId(id));
    }

    @Operation(summary = "Get geofence summaries for a store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found geofence summaries"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{id}/geofence-summary")
    public ResponseEntity<List<StoreGeofenceSummaryResponse>> getGeofenceSummary(@PathVariable Long id) {
        return ResponseEntity.ok(storeQueryService.findGeofenceSummariesByStoreId(id));
    }

    @Operation(summary = "Get order summaries for a store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order summaries"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{id}/order-summary")
    public ResponseEntity<List<StoreOrderSummaryResponse>> getOrderSummary(@PathVariable Long id) {
        return ResponseEntity.ok(storeQueryService.findOrderSummariesByStoreId(id));
    }

    @Operation(summary = "Get task summaries for a store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found task summaries"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{id}/task-summary")
    public ResponseEntity<List<StoreTaskSummaryResponse>> getTaskSummary(@PathVariable Long id) {
        return ResponseEntity.ok(storeQueryService.findTaskSummariesByStoreId(id));
    }

    @Operation(summary = "Add a catalog item to a store's offerings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catalog item added successfully"),
            @ApiResponse(responseCode = "404", description = "Store or Catalog item not found")
    })
    @PutMapping("/{storeId}/catalog/{catalogId}")
    public ResponseEntity<Void> addCatalogItemToStore(
            @PathVariable Long storeId,
            @PathVariable Long catalogId) {
        storeService.addCatalogItemToStore(storeId, catalogId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove a catalog item from a store's offerings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Catalog item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Store offering not found")
    })
    @DeleteMapping("/{storeId}/catalog/{catalogId}")
    public ResponseEntity<Void> removeCatalogItemFromStore(
            @PathVariable Long storeId,
            @PathVariable Long catalogId) {
        storeService.removeCatalogItemFromStore(storeId, catalogId);
        return ResponseEntity.noContent().build();
    }

    // ADD THIS NEW ENDPOINT
    @Operation(summary = "Assign a geofence to a store's service area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geofence assignment process initiated successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @PutMapping("/{storeId}/geofences/{geofenceId}")
    public ResponseEntity<Void> assignGeofenceToStore(
            @PathVariable Long storeId,
            @PathVariable Long geofenceId) {
        storeService.assignGeofenceToStore(storeId, geofenceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove a geofence from a store's service area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Geofence removed successfully from store"),
            @ApiResponse(responseCode = "404", description = "Store or geofence association not found")
    })
    @DeleteMapping("/{storeId}/geofences/{geofenceId}")
    public ResponseEntity<Void> removeGeofenceFromStore(
            @PathVariable Long storeId,
            @PathVariable Long geofenceId) {
        storeService.removeGeofenceFromStore(storeId, geofenceId);
        return ResponseEntity.noContent().build();
    }
}
