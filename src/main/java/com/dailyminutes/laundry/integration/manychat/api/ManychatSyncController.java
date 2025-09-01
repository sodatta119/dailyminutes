/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.manychat.api;

// com.dailyminutes.laundry.integration.tookan.SimpleSyncController.java

import com.dailyminutes.laundry.integration.manychat.service.CustomerSyncService;
import com.dailyminutes.laundry.integration.tookan.service.AgentSyncService;
import com.dailyminutes.laundry.integration.tookan.service.GeofenceSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * The type Manychat sync controller.
 */
@RestController
@RequestMapping("/api/v1/manychat/sync")
@RequiredArgsConstructor
@Slf4j
public class ManychatSyncController {
    private final CustomerSyncService customerSync;
    private final GeofenceSyncService geofenceSync;
    private final AgentSyncService agentSyncService;

    /**
     * Teams response entity.
     *
     * @return the response entity
     */
    @PostMapping("/customers")
    @Operation(summary = "Sync teams from Tookan")
    @ApiResponse(responseCode = "202", description = "Sync job triggered successfully")
    public ResponseEntity<Map<String, Object>> teams() {
        try{
            customerSync.syncCustomers();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("entity", "TEAM", "entities", "customers"));
        }catch (Exception e) {
            // Log the full error on the server side for debugging
            log.error("Error during Tookan sync operation", e);

            // Create a user-friendly error response body
            Map<String, Object> errorBody = Map.of(
                    "message", "An error occurred while communicating with the logistics service.",
                    "error", e.getMessage() // Be careful about leaking internal details
            );

            // Return a proper HTTP status code.
            // This response will now correctly pass through your CORS filter.
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorBody);
        }
    }

}
