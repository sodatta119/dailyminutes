/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.api;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.dto.*;
import com.dailyminutes.laundry.agent.service.AgentQueryService;
import com.dailyminutes.laundry.agent.service.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST Controller for managing Agent resources.
 * Exposes endpoints for CRUD operations on agents and querying agent-related summaries.
 */
@RestController
@RequestMapping("/agents") // Base path for all agent-related endpoints
@RequiredArgsConstructor // Lombok annotation for constructor injection
public class AgentController {

    private final AgentService agentService;
    private final AgentQueryService agentQueryService;

    /**
     * Creates a new agent.
     *
     * @param request The DTO containing the details of the agent to create.
     * @return A ResponseEntity with the created AgentResponse DTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<AgentResponse> createAgent(@Valid @RequestBody CreateAgentRequest request) {
        try {
            AgentResponse response = agentService.createAgent(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Handle validation or business rule exceptions
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Updates an existing agent.
     *
     * @param id The ID of the agent to update.
     * @param request The DTO containing the updated details of the agent.
     * @return A ResponseEntity with the updated AgentResponse DTO and HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgentResponse> updateAgent(@PathVariable Long id, @Valid @RequestBody UpdateAgentRequest request) {
        // Ensure the ID in the path matches the ID in the request body for consistency
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        try {
            AgentResponse response = agentService.updateAgent(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle cases where agent not found or other business rule violations
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Deletes an agent by ID.
     *
     * @param id The ID of the agent to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        try {
            agentService.deleteAgent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Handle case where agent not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Retrieves an agent by ID.
     *
     * @param id The ID of the agent to retrieve.
     * @return A ResponseEntity with the AgentResponse DTO and HTTP status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgentResponse> getAgentById(@PathVariable Long id) {
        return agentQueryService.findAgentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent with ID " + id + " not found."));
    }

    /**
     * Retrieves all agents.
     *
     * @return A ResponseEntity with a list of AgentResponse DTOs and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<AgentResponse>> getAllAgents() {
        List<AgentResponse> agents = agentQueryService.findAllAgents();
        return ResponseEntity.ok(agents);
    }

    /**
     * Retrieves agents by team ID.
     *
     * @param teamId The ID of the team.
     * @return A ResponseEntity with a list of AgentResponse DTOs and HTTP status 200 (OK).
     */
    @GetMapping("/by-team/{teamId}")
    public ResponseEntity<List<AgentResponse>> getAgentsByTeamId(@PathVariable Long teamId) {
        List<AgentResponse> agents = agentQueryService.findAgentsByTeamId(teamId);
        if (agents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No agents found for team ID " + teamId);
        }
        return ResponseEntity.ok(agents);
    }

    /**
     * Retrieves agents by their state.
     *
     * @param state The state of the agent (e.g., ACTIVE, INACTIVE).
     * @return A ResponseEntity with a list of AgentResponse DTOs and HTTP status 200 (OK).
     */
    @GetMapping("/by-state/{state}")
    public ResponseEntity<List<AgentResponse>> getAgentsByState(@PathVariable AgentState state) {
        List<AgentResponse> agents = agentQueryService.findAgentsByState(state);
        if (agents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No agents found with state " + state);
        }
        return ResponseEntity.ok(agents);
    }

    /**
     * Retrieves agents by their designation.
     *
     * @param designation The designation of the agent (e.g., FLEET_AGENT, DELIVERY_EXECUTIVE).
     * @return A ResponseEntity with a list of AgentResponse DTOs and HTTP status 200 (OK).
     */
    @GetMapping("/by-designation/{designation}")
    public ResponseEntity<List<AgentResponse>> getAgentsByDesignation(@PathVariable AgentDesignation designation) {
        List<AgentResponse> agents = agentQueryService.findAgentsByDesignation(designation);
        if (agents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No agents found with designation " + designation);
        }
        return ResponseEntity.ok(agents);
    }

    /**
     * Retrieves agent-team summaries by agent ID.
     *
     * @param agentId The ID of the agent.
     * @return A ResponseEntity with a list of AgentTeamSummaryResponse DTOs and HTTP status 200 (OK).
     */
    @GetMapping("/{agentId}/team-summaries")
    public ResponseEntity<List<AgentTeamSummaryResponse>> getAgentTeamSummaries(@PathVariable Long agentId) {
        List<AgentTeamSummaryResponse> summaries = agentQueryService.findAgentTeamSummariesByAgentId(agentId);
        if (summaries.isEmpty()) {
            // It's possible an agent exists but has no team summary if the event hasn't propagated yet
            // or if they are genuinely not associated with any team in the summary view.
            // Consider if NOT_FOUND is appropriate here, or an empty list with OK.
            // For now, returning NOT_FOUND if no summaries are found for the agent.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No team summaries found for agent ID " + agentId);
        }
        return ResponseEntity.ok(summaries);
    }

    /**
     * Retrieves agent-task summaries by agent ID.
     *
     * @param agentId The ID of the agent.
     * @return A ResponseEntity with a list of AgentTaskSummaryResponse DTOs and HTTP status 200 (OK).
     */
    @GetMapping("/{agentId}/task-summaries")
    public ResponseEntity<List<AgentTaskSummaryResponse>> getAgentTaskSummaries(@PathVariable Long agentId) {
        List<AgentTaskSummaryResponse> summaries = agentQueryService.findAgentTaskSummariesByAgentId(agentId);
        if (summaries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No task summaries found for agent ID " + agentId);
        }
        return ResponseEntity.ok(summaries);
    }
}
