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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgentQueryService {

    private final AgentRepository agentRepository;
    private final AgentTaskSummaryRepository agentTaskSummaryRepository;

    public Optional<AgentResponse> findAgentById(Long id) {
        return agentRepository.findById(id)
                .map(a -> new AgentResponse(a.getId(), a.getName(), a.getState(), a.getTeamId(), a.getPhoneNumber(), a.getUniqueId(), a.getJoiningDate(), a.getTerminationDate(), a.getDesignation()));
    }

    public List<AgentResponse> findAllAgents() {
        return StreamSupport.stream(agentRepository.findAll().spliterator(), false)
                .map(a -> new AgentResponse(a.getId(), a.getName(), a.getState(), a.getTeamId(), a.getPhoneNumber(), a.getUniqueId(), a.getJoiningDate(), a.getTerminationDate(), a.getDesignation()))
                .collect(Collectors.toList());
    }

    public List<AgentResponse> findAgentsByTeamId(Long teamId) {
        return agentRepository.findByTeamId(teamId).stream()
                .map(a -> new AgentResponse(a.getId(), a.getName(), a.getState(), a.getTeamId(), a.getPhoneNumber(), a.getUniqueId(), a.getJoiningDate(), a.getTerminationDate(), a.getDesignation()))
                .collect(Collectors.toList());
    }

    public List<AgentTaskSummaryResponse> findAgentTaskSummariesByAgentId(Long agentId) {
        return agentTaskSummaryRepository.findByAgentId(agentId).stream()
                .map(s -> new AgentTaskSummaryResponse(s.getTaskId(), s.getAgentId(), s.getTaskName(), s.getTaskType(), s.getTaskStatus(), s.getTaskStartTime(), s.getSourceAddress(), s.getDestinationAddress(), s.getOrderId()))
                .collect(Collectors.toList());
    }
}