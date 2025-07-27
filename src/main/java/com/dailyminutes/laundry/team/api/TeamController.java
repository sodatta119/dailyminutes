// FILE: src/main/java/com/dailyminutes/laundry/team/api/TeamController.java
package com.dailyminutes.laundry.team.api;

import com.dailyminutes.laundry.team.dto.CreateTeamRequest;
import com.dailyminutes.laundry.team.dto.TeamAgentSummaryResponse;
import com.dailyminutes.laundry.team.dto.TeamResponse;
import com.dailyminutes.laundry.team.dto.TeamTaskSummaryResponse;
import com.dailyminutes.laundry.team.dto.UpdateTeamRequest;
import com.dailyminutes.laundry.team.service.TeamQueryService;
import com.dailyminutes.laundry.team.service.TeamService;
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
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Team Management", description = "APIs for managing teams")
public class TeamController {

    private final TeamService teamService;
    private final TeamQueryService teamQueryService;

    @Operation(summary = "Create a new team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Team created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        return new ResponseEntity<>(teamService.createTeam(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long id, @Valid @RequestBody UpdateTeamRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(teamService.updateTeam(request));
    }

    @Operation(summary = "Delete a team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Team deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a team by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the team"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long id) {
        return teamQueryService.findTeamById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
    }

    @Operation(summary = "Get all teams")
    @ApiResponse(responseCode = "200", description = "List of all teams")
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(teamQueryService.findAllTeams());
    }

    @Operation(summary = "Get agent summaries for a team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found agent summaries"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{id}/agent-summary")
    public ResponseEntity<List<TeamAgentSummaryResponse>> getAgentSummary(@PathVariable Long id) {
        return ResponseEntity.ok(teamQueryService.findAgentSummariesByTeamId(id));
    }

    @Operation(summary = "Get task summaries for a team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found task summaries"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{id}/task-summary")
    public ResponseEntity<List<TeamTaskSummaryResponse>> getTaskSummary(@PathVariable Long id) {
        return ResponseEntity.ok(teamQueryService.findTaskSummariesByTeamId(id));
    }
}
