/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager.api;


import com.dailyminutes.laundry.manager.dto.CreateManagerRequest;
import com.dailyminutes.laundry.manager.dto.ManagerResponse;
import com.dailyminutes.laundry.manager.dto.UpdateManagerRequest;
import com.dailyminutes.laundry.manager.service.ManagerQueryService;
import com.dailyminutes.laundry.manager.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final ManagerQueryService managerQueryService;

    @PostMapping
    public ResponseEntity<ManagerResponse> createManager(@Valid @RequestBody CreateManagerRequest request) {
        return new ResponseEntity<>(managerService.createManager(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponse> updateManager(@PathVariable Long id, @Valid @RequestBody UpdateManagerRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(managerService.updateManager(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponse> getManagerById(@PathVariable Long id) {
        return managerQueryService.findManagerById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));
    }

    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getAllManagers() {
        return ResponseEntity.ok(managerQueryService.findAllManagers());
    }
}
