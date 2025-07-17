package com.dailyminutes.laundry.agent.service;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.domain.model.AgentTaskSummaryEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentTeamSummaryEntity;
import com.dailyminutes.laundry.agent.dto.AgentResponse;
import com.dailyminutes.laundry.agent.dto.AgentTaskSummaryResponse;
import com.dailyminutes.laundry.agent.dto.AgentTeamSummaryResponse;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import com.dailyminutes.laundry.agent.repository.AgentTaskSummaryRepository;
import com.dailyminutes.laundry.agent.repository.AgentTeamSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Enables Mockito annotations
class AgentQueryServiceTest {

    @Mock // Mocks the AgentRepository dependency
    private AgentRepository agentRepository;

    @Mock // Mocks the AgentTeamSummaryRepository dependency
    private AgentTeamSummaryRepository agentTeamSummaryRepository;

    @Mock // Mocks the AgentTaskSummaryRepository dependency
    private AgentTaskSummaryRepository agentTaskSummaryRepository;

    @InjectMocks // Injects the mocks into AgentQueryService
    private AgentQueryService agentQueryService;

    // Common data for tests
    private AgentEntity agent1;
    private AgentEntity agent2;
    private AgentTeamSummaryEntity teamSummary1;
    private AgentTaskSummaryEntity taskSummary1;

    @BeforeEach
    void setUp() {
        // Initialize common entities for tests
        agent1 = new AgentEntity(
                1L, "Agent One", AgentState.ACTIVE, 10L, "1112223333", "UID001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT);
        agent2 = new AgentEntity(
                2L, "Agent Two", AgentState.INACTIVE, 20L, "4445556666", "UID002", LocalDate.now(), LocalDate.now().plusDays(5), AgentDesignation.FLEET_AGENT);

        teamSummary1 = new AgentTeamSummaryEntity(
                100L, 10L, 1L, "Alpha Team", "Team for fleet operations");

        taskSummary1 = new AgentTaskSummaryEntity(
                1L,200L, 1L, "Pickup Task", "PICKUP", "NEW", LocalDateTime.now(), "Source A", "Dest B", 500L);
    }

    @Test
    void findAgentById_shouldReturnAgentResponse_whenFound() {
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent1));

        Optional<AgentResponse> result = agentQueryService.findAgentById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(agent1.getId());
        assertThat(result.get().name()).isEqualTo(agent1.getName());
    }

    @Test
    void findAgentById_shouldReturnEmptyOptional_whenNotFound() {
        when(agentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<AgentResponse> result = agentQueryService.findAgentById(99L);

        assertThat(result).isNotPresent();
    }

    @Test
    void findAllAgents_shouldReturnListOfAgentResponses() {
        when(agentRepository.findAll()).thenReturn(Arrays.asList(agent1, agent2));

        List<AgentResponse> results = agentQueryService.findAllAgents();

        assertThat(results).hasSize(2);
        assertThat(results.get(0).id()).isEqualTo(agent1.getId());
        assertThat(results.get(1).id()).isEqualTo(agent2.getId());
    }

    @Test
    void findAgentByPhoneNumber_shouldReturnAgentResponse_whenFound() {
        when(agentRepository.findByPhoneNumber("1112223333")).thenReturn(Optional.of(agent1));

        Optional<AgentResponse> result = agentQueryService.findAgentByPhoneNumber("1112223333");

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(agent1.getId());
        assertThat(result.get().phoneNumber()).isEqualTo(agent1.getPhoneNumber());
    }

    @Test
    void findAgentByPhoneNumber_shouldReturnEmptyOptional_whenNotFound() {
        when(agentRepository.findByPhoneNumber("0000000000")).thenReturn(Optional.empty());

        Optional<AgentResponse> result = agentQueryService.findAgentByPhoneNumber("0000000000");

        assertThat(result).isNotPresent();
    }

    @Test
    void findAgentByUniqueId_shouldReturnAgentResponse_whenFound() {
        when(agentRepository.findByUniqueId("UID001")).thenReturn(Optional.of(agent1));

        Optional<AgentResponse> result = agentQueryService.findAgentByUniqueId("UID001");

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(agent1.getId());
        assertThat(result.get().uniqueId()).isEqualTo(agent1.getUniqueId());
    }

    @Test
    void findAgentByUniqueId_shouldReturnEmptyOptional_whenNotFound() {
        when(agentRepository.findByUniqueId("NONEXISTENT")).thenReturn(Optional.empty());

        Optional<AgentResponse> result = agentQueryService.findAgentByUniqueId("NONEXISTENT");

        assertThat(result).isNotPresent();
    }

    @Test
    void findAgentsByTeamId_shouldReturnListOfAgentResponses() {
        when(agentRepository.findByTeamId(10L)).thenReturn(Collections.singletonList(agent1));

        List<AgentResponse> results = agentQueryService.findAgentsByTeamId(10L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo(agent1.getId());
        assertThat(results.get(0).teamId()).isEqualTo(10L);
    }

    @Test
    void findAgentsByState_shouldReturnListOfAgentResponses() {
        when(agentRepository.findByState(AgentState.ACTIVE)).thenReturn(Collections.singletonList(agent1));

        List<AgentResponse> results = agentQueryService.findAgentsByState(AgentState.ACTIVE);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo(agent1.getId());
        assertThat(results.get(0).state()).isEqualTo(AgentState.ACTIVE);
    }

    @Test
    void findAgentsByDesignation_shouldReturnListOfAgentResponses() {
        when(agentRepository.findByDesignation(AgentDesignation.FLEET_AGENT)).thenReturn(Collections.singletonList(agent1));

        List<AgentResponse> results = agentQueryService.findAgentsByDesignation(AgentDesignation.FLEET_AGENT);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo(agent1.getId());
        assertThat(results.get(0).designation()).isEqualTo(AgentDesignation.FLEET_AGENT);
    }

    @Test
    void findAgentTeamSummariesByAgentId_shouldReturnListOfAgentTeamSummaryResponses() {
        when(agentTeamSummaryRepository.findByAgentId(1L)).thenReturn(Collections.singletonList(teamSummary1));

        List<AgentTeamSummaryResponse> results = agentQueryService.findAgentTeamSummariesByAgentId(1L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo(teamSummary1.getId());
        assertThat(results.get(0).agentId()).isEqualTo(teamSummary1.getAgentId());
        assertThat(results.get(0).teamName()).isEqualTo(teamSummary1.getTeamName());
    }

    @Test
    void findAgentTaskSummariesByAgentId_shouldReturnListOfAgentTaskSummaryResponses() {
        when(agentTaskSummaryRepository.findByAgentId(1L)).thenReturn(Collections.singletonList(taskSummary1));

        List<AgentTaskSummaryResponse> results = agentQueryService.findAgentTaskSummariesByAgentId(1L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).taskId()).isEqualTo(taskSummary1.getTaskId());
        assertThat(results.get(0).agentId()).isEqualTo(taskSummary1.getAgentId());
        assertThat(results.get(0).taskName()).isEqualTo(taskSummary1.getTaskName());
    }
}
