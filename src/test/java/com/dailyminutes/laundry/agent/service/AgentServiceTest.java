package com.dailyminutes.laundry.agent.service;

import com.dailyminutes.laundry.agent.domain.event.AgentAssignedToTeamEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentCreatedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentDeletedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentUpdatedEvent;
import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.dto.CreateAgentRequest;
import com.dailyminutes.laundry.agent.dto.UpdateAgentRequest;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {

    @Mock
    private AgentRepository agentRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private AgentService agentService;

    @Test
    void createAgent_shouldCreateAndPublishEvent() {
        CreateAgentRequest request = new CreateAgentRequest("Test Agent", AgentState.ACTIVE, 1L, "1234567890", "unique1", LocalDate.now(), AgentDesignation.FLEET_AGENT);
        AgentEntity agent = new AgentEntity(1L, "Test Agent", AgentState.ACTIVE, 1L, "1234567890", "unique1", LocalDate.now(), null, AgentDesignation.FLEET_AGENT);
        when(agentRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());
        when(agentRepository.findByUniqueId(any())).thenReturn(Optional.empty());
        when(agentRepository.save(any())).thenReturn(agent);

        agentService.createAgent(request);

        verify(events).publishEvent(any(AgentCreatedEvent.class));
    }

    @Test
    void updateAgent_shouldUpdateAndPublishEvent() {
        UpdateAgentRequest request = new UpdateAgentRequest(1L, "Updated Agent", AgentState.INACTIVE, 2L, "0987654321", "unique2", LocalDate.now(), null, AgentDesignation.STORE_AGENT);
        AgentEntity agent = new AgentEntity(1L, "Test Agent", AgentState.ACTIVE, 1L, "1234567890", "unique1", LocalDate.now(), null, AgentDesignation.FLEET_AGENT);
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));
        when(agentRepository.save(any())).thenReturn(agent);

        agentService.updateAgent(request);

        verify(events).publishEvent(any(AgentUpdatedEvent.class));
    }

    @Test
    void deleteAgent_shouldDeleteAndPublishEvent() {
        when(agentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(agentRepository).deleteById(1L);

        agentService.deleteAgent(1L);

        verify(events).publishEvent(any(AgentDeletedEvent.class));
    }

    @Test
    void updateAgent_shouldThrowException_whenAgentNotFound() {
        UpdateAgentRequest request = new UpdateAgentRequest(1L, "Updated Agent", AgentState.INACTIVE, 2L, "0987654321", "unique2", LocalDate.now(), null, AgentDesignation.STORE_AGENT);
        when(agentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> agentService.updateAgent(request));
    }

    @Test
    void assignTeam_shouldUpdateTeamIdAndPublishEvent() {
        AgentEntity agent = new AgentEntity(1L, "Test Agent", AgentState.ACTIVE, 1L, "1234567890", "unique1", LocalDate.now(), null, AgentDesignation.FLEET_AGENT);
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));
        when(agentRepository.save(any())).thenReturn(agent);

        agentService.assignTeam(1L, 2L);

        verify(events).publishEvent(any(AgentAssignedToTeamEvent.class));
    }

//    @Test
//    void unassignTeam_shouldNullifyTeamIdAndPublishEvent() {
//        AgentEntity agent = new AgentEntity(1L, "Test Agent", AgentState.ACTIVE, 1L, "1234567890", "unique1", LocalDate.now(), null, AgentDesignation.FLEET_AGENT);
//        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));
//        when(agentRepository.save(any())).thenReturn(agent);
//
//        agentService.unassignTeam(1L);
//
//        verify(events).publishEvent(any(AgentAssignedToTeamEvent.class));
//    }
}