/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 17/07/25
 */
package com.dailyminutes.laundry.agent.service;

import com.dailyminutes.laundry.agent.domain.event.AgentAssignedToTeamEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentCreatedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentDeletedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentUpdatedEvent;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.dto.AgentResponse;
import com.dailyminutes.laundry.agent.dto.CreateAgentRequest;
import com.dailyminutes.laundry.agent.dto.UpdateAgentRequest;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * The type Agent service.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AgentService {

    private final AgentRepository agentRepository;
    private final ApplicationEventPublisher events;

    /**
     * Create agent agent response.
     *
     * @param request the request
     * @return the agent response
     */
    public AgentResponse createAgent(CreateAgentRequest request) {
        agentRepository.findByPhoneNumber(request.phoneNumber()).ifPresent(agent -> {
            throw new IllegalArgumentException("Agent with phone number " + request.phoneNumber() + " already exists.");
        });
        agentRepository.findByUniqueId(request.uniqueId()).ifPresent(agent -> {
            throw new IllegalArgumentException("Agent with unique ID " + request.uniqueId() + " already exists.");
        });

        AgentEntity agent = new AgentEntity(null, request.name(), request.state(), request.teamId(), request.phoneNumber(), request.uniqueId(), request.joiningDate(), null, request.designation(), null, LocalDateTime.now(), false);
        AgentEntity savedAgent = agentRepository.save(agent);

        events.publishEvent(new AgentCreatedEvent(savedAgent.getId(), savedAgent.getName(), savedAgent.getState().name(), savedAgent.getTeamId(), savedAgent.getPhoneNumber(), savedAgent.getUniqueId(), savedAgent.getJoiningDate(), savedAgent.getDesignation().name()));

        return toAgentResponse(savedAgent);
    }

    /**
     * Update agent agent response.
     *
     * @param request the request
     * @return the agent response
     */
    public AgentResponse updateAgent(UpdateAgentRequest request) {
        AgentEntity existingAgent = agentRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Agent with ID " + request.id() + " not found."));

        Long oldTeamId = existingAgent.getTeamId();

        existingAgent.setName(request.name());
        existingAgent.setState(request.state());
        existingAgent.setTeamId(request.teamId());
        existingAgent.setPhoneNumber(request.phoneNumber());
        existingAgent.setUniqueId(request.uniqueId());
        existingAgent.setJoiningDate(request.joiningDate());
        existingAgent.setTerminationDate(request.terminationDate());
        existingAgent.setDesignation(request.designation());

        AgentEntity updatedAgent = agentRepository.save(existingAgent);

        events.publishEvent(new AgentUpdatedEvent(updatedAgent.getId(), updatedAgent.getName(), updatedAgent.getState().name(), updatedAgent.getTeamId(), updatedAgent.getPhoneNumber(), updatedAgent.getUniqueId(), updatedAgent.getJoiningDate(), updatedAgent.getTerminationDate(), updatedAgent.getDesignation().name()));

        if (!java.util.Objects.equals(oldTeamId, updatedAgent.getTeamId())) {
            events.publishEvent(new AgentAssignedToTeamEvent(updatedAgent.getId(), updatedAgent.getTeamId()));
        }

        return toAgentResponse(updatedAgent);
    }

    /**
     * Delete agent.
     *
     * @param id the id
     */
    public void deleteAgent(Long id) {
        if (!agentRepository.existsById(id)) {
            throw new IllegalArgumentException("Agent with ID " + id + " not found.");
        }
        agentRepository.deleteById(id);
        events.publishEvent(new AgentDeletedEvent(id));
    }

    /**
     * Assign team agent response.
     *
     * @param agentId the agent id
     * @param teamId  the team id
     * @return the agent response
     */
    public AgentResponse assignTeam(Long agentId, Long teamId) {
        AgentEntity agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent with ID " + agentId + " not found."));

        Long oldTeamId = agent.getTeamId();
        agent.setTeamId(teamId);
        AgentEntity updatedAgent = agentRepository.save(agent);

        if (!java.util.Objects.equals(oldTeamId, updatedAgent.getTeamId())) {
            events.publishEvent(new AgentAssignedToTeamEvent(updatedAgent.getId(), updatedAgent.getTeamId()));
        }

        return toAgentResponse(updatedAgent);
    }
//
//    public AgentResponse unassignTeam(Long agentId) {
//        AgentEntity agent = agentRepository.findById(agentId)
//                .orElseThrow(() -> new IllegalArgumentException("Agent with ID " + agentId + " not found."));
//
//        Long oldTeamId = agent.getTeamId();
//        agent.setTeamId(null);
//        AgentEntity updatedAgent = agentRepository.save(agent);
//
//        if (oldTeamId != null) {
//            events.publishEvent(new AgentAssignedToTeamEvent(updatedAgent.getId(), null));
//        }
//
//        return toAgentResponse(updatedAgent);
//    }

    private AgentResponse toAgentResponse(AgentEntity agentEntity) {
        return new AgentResponse(agentEntity.getId(), agentEntity.getName(), agentEntity.getState(), agentEntity.getTeamId(), agentEntity.getPhoneNumber(), agentEntity.getUniqueId(), agentEntity.getJoiningDate(), agentEntity.getTerminationDate(), agentEntity.getDesignation());
    }
}
