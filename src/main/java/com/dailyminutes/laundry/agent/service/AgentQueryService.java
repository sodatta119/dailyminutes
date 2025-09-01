/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.service;

import com.dailyminutes.laundry.agent.dto.AgentResponse;
import com.dailyminutes.laundry.agent.dto.AgentTaskSummaryResponse;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import com.dailyminutes.laundry.agent.repository.AgentTaskSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Agent query service.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgentQueryService {

    private final AgentRepository agentRepository;
    private final AgentTaskSummaryRepository agentTaskSummaryRepository;

    /**
     * Find agent by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<AgentResponse> findAgentById(Long id) {
        return agentRepository.findById(id)
                .map(a -> new AgentResponse(a.getId(), a.getName(), a.getState(), a.getTeamId(), a.getPhoneNumber(), a.getUniqueId(), a.getJoiningDate(), a.getTerminationDate(), a.getDesignation(), a.getLatitude(), a.getLongitude(), a.getActive(), a.getAvailable()));
    }

    /**
     * Find all agents list.
     *
     * @return the list
     */
    public List<AgentResponse> findAllAgents() {
        return StreamSupport.stream(agentRepository.findAll().spliterator(), false)
                .map(a -> new AgentResponse(a.getId(), a.getName(), a.getState(), a.getTeamId(), a.getPhoneNumber(), a.getUniqueId(), a.getJoiningDate(), a.getTerminationDate(), a.getDesignation(), a.getLatitude(), a.getLongitude(), a.getActive(), a.getAvailable()))
                .collect(Collectors.toList());
    }

    /**
     * Find agents by team id list.
     *
     * @param teamId the team id
     * @return the list
     */
    public List<AgentResponse> findAgentsByTeamId(Long teamId) {
        return agentRepository.findByTeamId(teamId).stream()
                .map(a -> new AgentResponse(a.getId(), a.getName(), a.getState(), a.getTeamId(), a.getPhoneNumber(), a.getUniqueId(), a.getJoiningDate(), a.getTerminationDate(), a.getDesignation(), a.getLatitude(), a.getLongitude(), a.getActive(), a.getAvailable()))
                .collect(Collectors.toList());
    }

    /**
     * Find agent task summaries by agent id list.
     *
     * @param agentId the agent id
     * @return the list
     */
    public List<AgentTaskSummaryResponse> findAgentTaskSummariesByAgentId(Long agentId) {
        return agentTaskSummaryRepository.findByAgentId(agentId).stream()
                .map(s -> new AgentTaskSummaryResponse(s.getTaskId(), s.getAgentId(), s.getTaskName(), s.getTaskType(), s.getTaskStatus(), s.getTaskStartTime(), s.getSourceAddress(), s.getDestinationAddress(), s.getOrderId()))
                .collect(Collectors.toList());
    }
}