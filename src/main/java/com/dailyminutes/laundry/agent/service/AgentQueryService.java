/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.service;

import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import com.dailyminutes.laundry.agent.repository.AgentTeamSummaryRepository;
import com.dailyminutes.laundry.agent.repository.AgentTaskSummaryRepository;
import com.dailyminutes.laundry.agent.dto.AgentResponse;
import com.dailyminutes.laundry.agent.dto.AgentTeamSummaryResponse;
import com.dailyminutes.laundry.agent.dto.AgentTaskSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service for querying Agent-related data.
 * This is the query-side service, responsible for retrieving agent information
 * from the authoritative AgentEntity and its denormalized read models.
 */
@Service
@RequiredArgsConstructor // Lombok annotation for constructor injection of final fields
@Transactional(readOnly = true) // Optimize for read operations
public class AgentQueryService {

    private final AgentRepository agentRepository;
    private final AgentTeamSummaryRepository agentTeamSummaryRepository;
    private final AgentTaskSummaryRepository agentTaskSummaryRepository;

    /**
     * Finds an agent by their ID from the authoritative AgentEntity.
     *
     * @param id The ID of the agent.
     * @return An Optional containing the AgentResponse DTO if found, empty otherwise.
     */
    public Optional<AgentResponse> findAgentById(Long id) {
        return agentRepository.findById(id)
                .map(this::toAgentResponse);
    }

    /**
     * Finds all agents from the authoritative AgentEntity.
     *
     * @return A list of all AgentResponse DTOs.
     */
    public List<AgentResponse> findAllAgents() {
        return StreamSupport.stream(agentRepository.findAll().spliterator(), false)
                .map(this::toAgentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Finds agents by their phone number from the authoritative AgentEntity.
     *
     * @param phoneNumber The phone number of the agent.
     * @return An Optional containing the AgentResponse DTO if found, empty otherwise.
     */
    public Optional<AgentResponse> findAgentByPhoneNumber(String phoneNumber) {
        return agentRepository.findByPhoneNumber(phoneNumber)
                .map(this::toAgentResponse);
    }

    /**
     * Finds agents by their unique ID from the authoritative AgentEntity.
     *
     * @param uniqueId The unique ID of the agent.
     * @return An Optional containing the AgentResponse DTO if found, empty otherwise.
     */
    public Optional<AgentResponse> findAgentByUniqueId(String uniqueId) {
        return agentRepository.findByUniqueId(uniqueId)
                .map(this::toAgentResponse);
    }

    /**
     * Finds agents by their team ID from the authoritative AgentEntity.
     *
     * @param teamId The ID of the team.
     * @return A list of AgentResponse DTOs for agents belonging to the specified team.
     */
    public List<AgentResponse> findAgentsByTeamId(Long teamId) {
        return agentRepository.findByTeamId(teamId).stream()
                .map(this::toAgentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Finds agents by their state from the authoritative AgentEntity.
     *
     * @param state The state of the agent (e.g., ACTIVE, INACTIVE).
     * @return A list of AgentResponse DTOs for agents in the specified state.
     */
    public List<AgentResponse> findAgentsByState(AgentState state) {
        return agentRepository.findByState(state).stream()
                .map(this::toAgentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Finds agents by their designation from the authoritative AgentEntity.
     *
     * @param designation The designation of the agent (e.g., FLEET_AGENT, DELIVERY_EXECUTIVE).
     * @return A list of AgentResponse DTOs for agents with the specified designation.
     */
    public List<AgentResponse> findAgentsByDesignation(AgentDesignation designation) {
        return agentRepository.findByDesignation(designation).stream()
                .map(this::toAgentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Finds agent-team summaries by agent ID from the read model.
     *
     * @param agentId The ID of the agent.
     * @return A list of AgentTeamSummaryResponse DTOs.
     */
    public List<AgentTeamSummaryResponse> findAgentTeamSummariesByAgentId(Long agentId) {
        return agentTeamSummaryRepository.findByAgentId(agentId).stream()
                .map(summary -> new AgentTeamSummaryResponse(
                        summary.getId(),
                        summary.getTeamId(),
                        summary.getAgentId(),
                        summary.getTeamName(),
                        summary.getTeamDescription()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Finds agent-task summaries by agent ID from the read model.
     *
     * @param agentId The ID of the agent.
     * @return A list of AgentTaskSummaryResponse DTOs.
     */
    public List<AgentTaskSummaryResponse> findAgentTaskSummariesByAgentId(Long agentId) {
        return agentTaskSummaryRepository.findByAgentId(agentId).stream()
                .map(summary -> new AgentTaskSummaryResponse(
                        summary.getTaskId(),
                        summary.getAgentId(),
                        summary.getTaskName(),
                        summary.getTaskType(),
                        summary.getTaskStatus(),
                        summary.getTaskStartTime(),
                        summary.getSourceAddress(),
                        summary.getDestinationAddress(),
                        summary.getOrderId()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Helper method to convert AgentEntity to AgentResponse DTO.
     *
     * @param agentEntity The AgentEntity to convert.
     * @return The corresponding AgentResponse DTO.
     */
    private AgentResponse toAgentResponse(AgentEntity agentEntity) {
        return new AgentResponse(
                agentEntity.getId(),
                agentEntity.getName(),
                agentEntity.getState(),
                agentEntity.getTeamId(),
                agentEntity.getPhoneNumber(),
                agentEntity.getUniqueId(),
                agentEntity.getJoiningDate(),
                agentEntity.getTerminationDate(),
                agentEntity.getDesignation()
        );
    }
}
