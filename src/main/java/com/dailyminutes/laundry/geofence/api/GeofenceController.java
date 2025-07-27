/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.api;


import com.dailyminutes.laundry.geofence.dto.*;
import com.dailyminutes.laundry.geofence.service.GeofenceQueryService;
import com.dailyminutes.laundry.geofence.service.GeofenceService;
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
@RequestMapping("/api/geofences")
@RequiredArgsConstructor
@Tag(name = "Geofence Management", description = "APIs for managing geofences")
public class GeofenceController {

    private final GeofenceService geofenceService;
    private final GeofenceQueryService geofenceQueryService;

    @Operation(summary = "Create a new geofence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Geofence created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<GeofenceResponse> createGeofence(@Valid @RequestBody CreateGeofenceRequest request) {
        return new ResponseEntity<>(geofenceService.createGeofence(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing geofence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geofence updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Geofence not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GeofenceResponse> updateGeofence(@PathVariable Long id, @Valid @RequestBody UpdateGeofenceRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(geofenceService.updateGeofence(request));
    }

    @Operation(summary = "Delete a geofence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Geofence deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Geofence not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeofence(@PathVariable Long id) {
        geofenceService.deleteGeofence(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a geofence by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the geofence"),
            @ApiResponse(responseCode = "404", description = "Geofence not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GeofenceResponse> getGeofenceById(@PathVariable Long id) {
        return geofenceQueryService.findGeofenceById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Geofence not found"));
    }

    @Operation(summary = "Get all geofences")
    @ApiResponse(responseCode = "200", description = "List of all geofences")
    @GetMapping
    public ResponseEntity<List<GeofenceResponse>> getAllGeofences() {
        return ResponseEntity.ok(geofenceQueryService.findAllGeofences());
    }

    @Operation(summary = "Get store summaries for a geofence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found store summaries"),
            @ApiResponse(responseCode = "404", description = "Geofence not found")
    })
    @GetMapping("/{id}/stores")
    public ResponseEntity<List<GeofenceStoreSummaryResponse>> getStoreSummaries(@PathVariable Long id) {
        return ResponseEntity.ok(geofenceQueryService.findStoreSummariesByGeofenceId(id));
    }

    @Operation(summary = "Get customer summaries for a geofence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found customer summaries"),
            @ApiResponse(responseCode = "404", description = "Geofence not found")
    })
    @GetMapping("/{id}/customers")
    public ResponseEntity<List<GeofenceCustomerSummaryResponse>> getCustomerSummaries(@PathVariable Long id) {
        return ResponseEntity.ok(geofenceQueryService.findCustomerSummariesByGeofenceId(id));
    }

    @Operation(summary = "Get order summaries for a geofence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order summaries"),
            @ApiResponse(responseCode = "404", description = "Geofence not found")
    })
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<GeofenceOrderSummaryResponse>> getOrderSummaries(@PathVariable Long id) {
        return ResponseEntity.ok(geofenceQueryService.findOrderSummariesByGeofenceId(id));
    }
}
