/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.api;

import com.dailyminutes.laundry.agent.dto.*;
import com.dailyminutes.laundry.agent.service.AgentQueryService;
import com.dailyminutes.laundry.agent.service.AgentService;
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
 * The type Agent controller.
 */
@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
@Tag(name = "Agent Management", description = "APIs for managing agents")
public class AgentController {

    private final AgentService agentService;
    private final AgentQueryService agentQueryService;

    /**
     * Create agent response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Create a new agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agent created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<AgentResponse> createAgent(@Valid @RequestBody CreateAgentRequest request) {
        return new ResponseEntity<>(agentService.createAgent(request), HttpStatus.CREATED);
    }

    /**
     * Update agent response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Update an existing agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Agent not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AgentResponse> updateAgent(@PathVariable Long id, @Valid @RequestBody UpdateAgentRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(agentService.updateAgent(request));
    }

    /**
     * Delete agent response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Operation(summary = "Delete an agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agent deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Agent not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Gets agent by id.
     *
     * @param id the id
     * @return the agent by id
     */
    @Operation(summary = "Get an agent by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the agent"),
            @ApiResponse(responseCode = "404", description = "Agent not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AgentResponse> getAgentById(@PathVariable Long id) {
        return agentQueryService.findAgentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found"));
    }

    /**
     * Gets all agents.
     *
     * @return the all agents
     */
    @Operation(summary = "Get all agents")
    @ApiResponse(responseCode = "200", description = "List of all agents")
    @GetMapping
    public ResponseEntity<List<AgentResponse>> getAllAgents() {
        var agents=agentQueryService.findAllAgents();
        return ResponseEntity.ok(agents);
    }

    /**
     * Gets agents by team id.
     *
     * @param teamId the team id
     * @return the agents by team id
     */
    @Operation(summary = "Get agents by team ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found agents for the team"),
            @ApiResponse(responseCode = "404", description = "No agents found for the team")
    })
    @GetMapping("/by-team/{teamId}")
    public ResponseEntity<List<AgentResponse>> getAgentsByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(agentQueryService.findAgentsByTeamId(teamId));
    }

    /**
     * Assign team response entity.
     *
     * @param id     the id
     * @param teamId the team id
     * @return the response entity
     */
    @Operation(summary = "Assign a team to an agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Agent not found")
    })
    @PutMapping("/{id}/assign-team/{teamId}")
    public ResponseEntity<AgentResponse> assignTeam(@PathVariable Long id, @PathVariable Long teamId) {
        return ResponseEntity.ok(agentService.assignTeam(id, teamId));
    }


    /**
     * Gets task summary.
     *
     * @param id the id
     * @return the task summary
     */
    @Operation(summary = "Get task summary for an agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found task summary"),
            @ApiResponse(responseCode = "404", description = "Agent not found")
    })
    @GetMapping("/{id}/task-summary")
    public ResponseEntity<List<AgentTaskSummaryResponse>> getTaskSummary(@PathVariable Long id) {
        return ResponseEntity.ok(agentQueryService.findAgentTaskSummariesByAgentId(id));
    }
}