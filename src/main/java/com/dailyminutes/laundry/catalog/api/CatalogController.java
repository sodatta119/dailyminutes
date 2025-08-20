/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.api;


import com.dailyminutes.laundry.catalog.dto.*;
import com.dailyminutes.laundry.catalog.service.CatalogQueryService;
import com.dailyminutes.laundry.catalog.service.CatalogService;
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
@RequestMapping("/api/catalogs")
@RequiredArgsConstructor
@Tag(name = "Catalog Management", description = "APIs for managing the service and product catalog")
public class CatalogController {

    private final CatalogService catalogService;
    private final CatalogQueryService catalogQueryService;

    @Operation(summary = "Create a new catalog item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Catalog item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CatalogResponse> createCatalog(@Valid @RequestBody CreateCatalogRequest request) {
        CatalogResponse response = catalogService.createCatalog(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing catalog item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catalog item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Catalog item not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CatalogResponse> updateCatalog(@PathVariable Long id, @Valid @RequestBody UpdateCatalogRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        try {
            CatalogResponse response = catalogService.updateCatalog(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Operation(summary = "Delete a catalog item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Catalog item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Catalog item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long id) {
        try {
            catalogService.deleteCatalog(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Operation(summary = "Get a catalog item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the catalog item"),
            @ApiResponse(responseCode = "404", description = "Catalog item not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CatalogResponse> getCatalogById(@PathVariable Long id) {
        return catalogQueryService.findCatalogById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalog with ID " + id + " not found."));
    }

    @Operation(summary = "Get all catalog items")
    @ApiResponse(responseCode = "200", description = "List of all catalog items")
    @GetMapping
    public ResponseEntity<List<CatalogResponse>> getAllCatalogs() {
        List<CatalogResponse> catalogs = catalogQueryService.findAllCatalogs();
        return ResponseEntity.ok(catalogs);
    }

    @Operation(summary = "Get order item history for a specific catalog item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order item history"),
            @ApiResponse(responseCode = "404", description = "Catalog item not found or never ordered")
    })
    @GetMapping("/{id}/order-items")
    public ResponseEntity<List<CatalogOrderItemSummaryResponse>> getOrderItemSummariesByCatalogId(@PathVariable Long id) {
        List<CatalogOrderItemSummaryResponse> items = catalogQueryService.findOrderItemSummariesByCatalogId(id);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
}
