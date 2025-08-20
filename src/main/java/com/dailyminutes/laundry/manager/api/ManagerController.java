// FILE: src/main/java/com/dailyminutes/laundry/manager/api/ManagerController.java
package com.dailyminutes.laundry.manager.api;

import com.dailyminutes.laundry.manager.dto.CreateManagerRequest;
import com.dailyminutes.laundry.manager.dto.ManagerResponse;
import com.dailyminutes.laundry.manager.dto.UpdateManagerRequest;
import com.dailyminutes.laundry.manager.service.ManagerQueryService;
import com.dailyminutes.laundry.manager.service.ManagerService;
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

/**
 * The type Manager controller.
 */
@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@Tag(name = "Manager Management", description = "APIs for managing managers")
public class ManagerController {

    private final ManagerService managerService;
    private final ManagerQueryService managerQueryService;

    /**
     * Create manager response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Create a new manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Manager created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ManagerResponse> createManager(@Valid @RequestBody CreateManagerRequest request) {
        return new ResponseEntity<>(managerService.createManager(request), HttpStatus.CREATED);
    }

    /**
     * Update manager response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Update an existing manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponse> updateManager(@PathVariable Long id, @Valid @RequestBody UpdateManagerRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(managerService.updateManager(request));
    }

    /**
     * Delete manager response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Operation(summary = "Delete a manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Manager deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Gets manager by id.
     *
     * @param id the id
     * @return the manager by id
     */
    @Operation(summary = "Get a manager by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the manager"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponse> getManagerById(@PathVariable Long id) {
        return managerQueryService.findManagerById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));
    }

    /**
     * Gets all managers.
     *
     * @return the all managers
     */
    @Operation(summary = "Get all managers")
    @ApiResponse(responseCode = "200", description = "List of all managers")
    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getAllManagers() {
        return ResponseEntity.ok(managerQueryService.findAllManagers());
    }
}
