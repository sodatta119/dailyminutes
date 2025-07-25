/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.api;


import com.dailyminutes.laundry.geofence.dto.*;
import com.dailyminutes.laundry.geofence.service.GeofenceQueryService;
import com.dailyminutes.laundry.geofence.service.GeofenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/geofences")
@RequiredArgsConstructor
public class GeofenceController {

    private final GeofenceService geofenceService;
    private final GeofenceQueryService geofenceQueryService;

    @PostMapping
    public ResponseEntity<GeofenceResponse> createGeofence(@Valid @RequestBody CreateGeofenceRequest request) {
        return new ResponseEntity<>(geofenceService.createGeofence(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeofenceResponse> updateGeofence(@PathVariable Long id, @Valid @RequestBody UpdateGeofenceRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(geofenceService.updateGeofence(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeofence(@PathVariable Long id) {
        geofenceService.deleteGeofence(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeofenceResponse> getGeofenceById(@PathVariable Long id) {
        return geofenceQueryService.findGeofenceById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Geofence not found"));
    }

    @GetMapping
    public ResponseEntity<List<GeofenceResponse>> getAllGeofences() {
        return ResponseEntity.ok(geofenceQueryService.findAllGeofences());
    }

    @GetMapping("/{id}/stores")
    public ResponseEntity<List<GeofenceStoreSummaryResponse>> getStoreSummaries(@PathVariable Long id) {
        return ResponseEntity.ok(geofenceQueryService.findStoreSummariesByGeofenceId(id));
    }

    @GetMapping("/{id}/customers")
    public ResponseEntity<List<GeofenceCustomerSummaryResponse>> getCustomerSummaries(@PathVariable Long id) {
        return ResponseEntity.ok(geofenceQueryService.findCustomerSummariesByGeofenceId(id));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<GeofenceOrderSummaryResponse>> getOrderSummaries(@PathVariable Long id) {
        return ResponseEntity.ok(geofenceQueryService.findOrderSummariesByGeofenceId(id));
    }
}
