package com.dailyminutes.laundry.team.listener;

import com.dailyminutes.laundry.agent.domain.event.AgentCreatedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentDeletedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentUpdatedEvent;
import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.team.domain.model.TeamAgentSummaryEntity;
import com.dailyminutes.laundry.team.repository.TeamAgentSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * The type Team agent event listener test.
 */
@ExtendWith(MockitoExtension.class)
class TeamAgentEventListenerTest {

    @Mock
    private TeamAgentSummaryRepository summaryRepository;

    @InjectMocks
    private TeamAgentEventListener listener;

    /**
     * On agent created should create summary.
     */
    @Test
    void onAgentCreated_shouldCreateSummary() {
        // Given
        AgentCreatedEvent event = new AgentCreatedEvent(1L, "Test Agent", AgentState.ACTIVE.name(), 10L,
                "555-1234", "unique-1", LocalDate.now(), AgentDesignation.FLEET_AGENT.name());
        ArgumentCaptor<TeamAgentSummaryEntity> captor = ArgumentCaptor.forClass(TeamAgentSummaryEntity.class);

        // When
        listener.onAgentCreated(event);

        // Then
        verify(summaryRepository).save(captor.capture());
        TeamAgentSummaryEntity summary = captor.getValue();
        assertThat(summary.getAgentId()).isEqualTo(1L);
        assertThat(summary.getTeamId()).isEqualTo(10L);
        assertThat(summary.getAgentName()).isEqualTo("Test Agent");
        assertThat(summary.getAgentState()).isEqualTo(AgentState.ACTIVE.name());
    }

    /**
     * On agent updated should update agent details.
     */
    @Test
    void onAgentUpdated_shouldUpdateAgentDetails() {
        // Given: An update event where the team ID has NOT changed
        AgentUpdatedEvent event = new AgentUpdatedEvent(1L, "Agent Updated Name", AgentState.INACTIVE.name(), 10L,
                "555-5678", "unique-1", LocalDate.now(), null, AgentDesignation.STORE_AGENT.name());
        TeamAgentSummaryEntity existingSummary = new TeamAgentSummaryEntity(
                99L, 10L, 1L, "Test Agent", "555-1234",
                AgentDesignation.FLEET_AGENT.name(), AgentState.ACTIVE.name());
        when(summaryRepository.findByAgentId(1L)).thenReturn(Optional.of(existingSummary));

        // When
        listener.onAgentUpdated(event);

        // Then
        verify(summaryRepository).save(existingSummary);
        assertThat(existingSummary.getAgentName()).isEqualTo("Agent Updated Name");
        assertThat(existingSummary.getAgentState()).isEqualTo(AgentState.INACTIVE.name());
        assertThat(existingSummary.getAgentPhoneNumber()).isEqualTo("555-5678");
    }

    /**
     * On agent updated should move agent to new team.
     */
    @Test
    void onAgentUpdated_shouldMoveAgentToNewTeam() {
        // Given: An update event where the team ID HAS changed (from 10L to 20L)
        AgentUpdatedEvent event = new AgentUpdatedEvent(1L, "Agent Moved", AgentState.ACTIVE.name(), 20L,
                "555-1234", "unique-1", LocalDate.now(), null, AgentDesignation.FLEET_AGENT.name());
        TeamAgentSummaryEntity existingSummary = new TeamAgentSummaryEntity(
                99L, 10L, 1L, "Test Agent", "555-1234",
                AgentDesignation.FLEET_AGENT.name(), AgentState.ACTIVE.name());
        when(summaryRepository.findByAgentId(1L)).thenReturn(Optional.of(existingSummary));
        ArgumentCaptor<TeamAgentSummaryEntity> captor = ArgumentCaptor.forClass(TeamAgentSummaryEntity.class);

        // When
        listener.onAgentUpdated(event);

        // Then: The old summary is deleted and a new one is created
        verify(summaryRepository).deleteById(99L);
        verify(summaryRepository).save(captor.capture());
        TeamAgentSummaryEntity newSummary = captor.getValue();
        assertThat(newSummary.getAgentId()).isEqualTo(1L);
        assertThat(newSummary.getTeamId()).isEqualTo(20L);
        assertThat(newSummary.getAgentName()).isEqualTo("Agent Moved");
    }

    /**
     * On agent deleted should delete summary.
     */
    @Test
    void onAgentDeleted_shouldDeleteSummary() {
        // Given
        AgentDeletedEvent event = new AgentDeletedEvent(1L);

        // When
        listener.onAgentDeleted(event);

        // Then
        verify(summaryRepository).deleteByAgentId(1L);
    }
}