package com.dailyminutes.laundry.agent.listeners;


import com.dailyminutes.laundry.agent.domain.event.AgentAssignedToTeamEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentCreatedEvent;
import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.domain.model.AgentTeamSummaryEntity;
import com.dailyminutes.laundry.agent.repository.AgentTeamSummaryRepository;
import com.dailyminutes.laundry.team.domain.event.TeamInfoRequestEvent;
import com.dailyminutes.laundry.team.domain.event.TeamInfoResponseEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentTeamEventListenerTest {

    @Mock
    private AgentTeamSummaryRepository agentTeamSummaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private AgentTeamEventListener listener;

    @Test
    void onAgentAssignedToTeam_shouldDeleteOldSummaryAndRequestNewTeamInfo() {
        // Given: An event indicating an agent has been assigned to a new team
        AgentAssignedToTeamEvent event = new AgentAssignedToTeamEvent(1L, 10L);
        AgentTeamSummaryEntity oldSummary = new AgentTeamSummaryEntity(99L, 1L, 5L, "Old Team", "Old Desc");

        when(agentTeamSummaryRepository.findByAgentId(1L)).thenReturn(Collections.singletonList(oldSummary));
        ArgumentCaptor<TeamInfoRequestEvent> eventCaptor = ArgumentCaptor.forClass(TeamInfoRequestEvent.class);

        // When: The listener handles the event
        listener.onAgentAssignedToTeam(event);

        // Then: The old summary should be deleted
        verify(agentTeamSummaryRepository).deleteById(99L);

        // And: A new event should be published to request info for the new team
        verify(events).publishEvent(eventCaptor.capture());
        TeamInfoRequestEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.agentId()).isEqualTo(1L);
        assertThat(publishedEvent.teamId()).isEqualTo(10L);
    }

    @Test
    void onAgentCreated_shouldRequestTeamInfo() {
        // Given: A new agent is created and assigned to team 10L
        AgentCreatedEvent event = new AgentCreatedEvent(
                1L, "New Agent", AgentState.ACTIVE, 10L, "1234567890",
                "unique-1", LocalDate.now(), AgentDesignation.FLEET_AGENT);

        ArgumentCaptor<TeamInfoRequestEvent> eventCaptor = ArgumentCaptor.forClass(TeamInfoRequestEvent.class);

        // When: The listener handles the creation event
        listener.onAgentCreated(event);

        // Then: A new event should be published to request info for team 10L
        verify(events).publishEvent(eventCaptor.capture());
        TeamInfoRequestEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.agentId()).isEqualTo(1L);
        assertThat(publishedEvent.teamId()).isEqualTo(10L);
    }

    /**
     * NEW EDGE CASE TEST
     * Verifies that if an agent is somehow created without a team,
     * no follow-up event is published.
     */
    @Test
    void onAgentCreated_shouldDoNothingIfTeamIdIsNull() {
        // Given: A new agent is created without a teamId
        AgentCreatedEvent event = new AgentCreatedEvent(
                1L, "New Agent", AgentState.ACTIVE, null, "1234567890",
                "unique-1", LocalDate.now(), AgentDesignation.FLEET_AGENT);

        // When: The listener handles the creation event
        listener.onAgentCreated(event);

        // Then: No event should be published
        verify(events, never()).publishEvent(any());
    }


    @Test
    void onAgentAssignedToTeam_shouldOnlyDeleteSummaryWhenUnassigned() {
        // Given: An event indicating an agent has been unassigned from a team (teamId is null)
        AgentAssignedToTeamEvent event = new AgentAssignedToTeamEvent(1L, null);
        AgentTeamSummaryEntity oldSummary = new AgentTeamSummaryEntity(99L, 1L, 5L, "Old Team", "Old Desc");

        when(agentTeamSummaryRepository.findByAgentId(1L)).thenReturn(Collections.singletonList(oldSummary));

        // When: The listener handles the event
        listener.onAgentAssignedToTeam(event);

        // Then: The old summary should be deleted
        verify(agentTeamSummaryRepository).deleteById(99L);

        // And: No new event should be published to request info
        verify(events, never()).publishEvent(any(TeamInfoRequestEvent.class));
    }

    @Test
    void onTeamInfoProvided_shouldCreateSummary() {
        // Given: An event from the team module providing team details
        TeamInfoResponseEvent event = new TeamInfoResponseEvent(1L, 10L, "New Team", "New Desc");
        ArgumentCaptor<AgentTeamSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(AgentTeamSummaryEntity.class);

        // When: The listener handles the event
        listener.onTeamInfoProvided(event);

        // Then: A new summary entity should be saved with the correct details
        verify(agentTeamSummaryRepository).save(summaryCaptor.capture());
        AgentTeamSummaryEntity savedSummary = summaryCaptor.getValue();
        assertThat(savedSummary.getAgentId()).isEqualTo(1L);
        assertThat(savedSummary.getTeamId()).isEqualTo(10L);
        assertThat(savedSummary.getTeamName()).isEqualTo("New Team");
        assertThat(savedSummary.getTeamDescription()).isEqualTo("New Desc");
    }
}