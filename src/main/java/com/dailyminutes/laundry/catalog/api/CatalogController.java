/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.api;


import com.dailyminutes.laundry.catalog.dto.*;
import com.dailyminutes.laundry.catalog.service.CatalogQueryService;
import com.dailyminutes.laundry.catalog.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final CatalogQueryService catalogQueryService;

    @PostMapping
    public ResponseEntity<CatalogResponse> createCatalog(@Valid @RequestBody CreateCatalogRequest request) {
        CatalogResponse response = catalogService.createCatalog(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long id) {
        try {
            catalogService.deleteCatalog(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogResponse> getCatalogById(@PathVariable Long id) {
        return catalogQueryService.findCatalogById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalog with ID " + id + " not found."));
    }

    @GetMapping
    public ResponseEntity<List<CatalogResponse>> getAllCatalogs() {
        List<CatalogResponse> catalogs = catalogQueryService.findAllCatalogs();
        return ResponseEntity.ok(catalogs);
    }

    @GetMapping("/{id}/store-offerings")
    public ResponseEntity<List<CatalogStoreOfferingSummaryResponse>> getStoreOfferingsByCatalogId(@PathVariable Long id) {
        List<CatalogStoreOfferingSummaryResponse> offerings = catalogQueryService.findStoreOfferingsByCatalogId(id);
        if (offerings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(offerings);
    }

    @GetMapping("/{id}/order-items")
    public ResponseEntity<List<CatalogOrderItemSummaryResponse>> getOrderItemSummariesByCatalogId(@PathVariable Long id) {
        List<CatalogOrderItemSummaryResponse> items = catalogQueryService.findOrderItemSummariesByCatalogId(id);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }
}
