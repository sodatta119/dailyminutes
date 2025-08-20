package com.dailyminutes.laundry.team.listener;

import com.dailyminutes.laundry.agent.domain.event.AgentInfoRequestEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentInfoResponseEvent;
import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.task.domain.event.TaskAssignedToAgentEvent;
import com.dailyminutes.laundry.task.domain.event.TaskCreatedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskDeletedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskStatusChangedEvent;
import com.dailyminutes.laundry.team.domain.model.TeamTaskSummaryEntity;
import com.dailyminutes.laundry.team.repository.TeamTaskSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamTaskEventListenerTest {

    @Mock
    private TeamTaskSummaryRepository summaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private TeamTaskEventListener listener;

    @Test
    void onTaskCreated_shouldCreateSummaryAndRequestAgentName_whenAgentIsAssigned() {
        // Given
        TaskCreatedEvent event = new TaskCreatedEvent(1L, 101L, "New Task", "PICKUP", "NEW",
                LocalDateTime.now(), "Addr A", 10L, "Addr B", 20L, 5L, 50L);
        ArgumentCaptor<TeamTaskSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(TeamTaskSummaryEntity.class);
        ArgumentCaptor<AgentInfoRequestEvent> requestCaptor = ArgumentCaptor.forClass(AgentInfoRequestEvent.class);

        // When
        listener.onTaskCreated(event);

        // Then
        verify(summaryRepository).save(summaryCaptor.capture());
        TeamTaskSummaryEntity summary = summaryCaptor.getValue();
        assertThat(summary.getTeamId()).isEqualTo(50L);
        assertThat(summary.getTaskId()).isEqualTo(1L);
        assertThat(summary.getAgentId()).isEqualTo(5L);
        assertThat(summary.getAgentName()).isNull();

        verify(events).publishEvent(requestCaptor.capture());
        AgentInfoRequestEvent request = requestCaptor.getValue();
        assertThat(request.agentId()).isEqualTo(5L);
        TaskCreatedEvent tEvent= (TaskCreatedEvent) request.originalEvent();
        assertThat(tEvent.taskId()).isEqualTo(1L);
    }

    @Test
    void onTaskCreated_shouldCreateSummaryButNotRequestAgentName_whenAgentIsNull() {
        // Given
        TaskCreatedEvent event = new TaskCreatedEvent(1L, 101L, "New Task", "PICKUP", "NEW",
                LocalDateTime.now(), "Addr A", 10L, "Addr B", 20L, null, 50L);
        ArgumentCaptor<TeamTaskSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(TeamTaskSummaryEntity.class);

        // When
        listener.onTaskCreated(event);

        // Then
        verify(summaryRepository).save(summaryCaptor.capture());
        assertThat(summaryCaptor.getValue().getAgentId()).isNull();

        verify(events, never()).publishEvent(any(AgentInfoRequestEvent.class));
    }

    @Test
    void onTaskCreated_shouldDoNothing_whenTeamIdIsNull() {
        // Given
        TaskCreatedEvent event = new TaskCreatedEvent(1L, 101L, "New Task", "PICKUP", "NEW",
                LocalDateTime.now(), "Addr A", 10L, "Addr B", 20L, 5L, null);

        // When
        listener.onTaskCreated(event);

        // Then
        verify(summaryRepository, never()).save(any());
        verify(events, never()).publishEvent(any());
    }

    @Test
    void onTaskAssignedToAgent_shouldUpdateSummaryAndRequestAgentName() {
        // Given
        TaskAssignedToAgentEvent event = new TaskAssignedToAgentEvent(1L, 7L);
        TeamTaskSummaryEntity existingSummary = new TeamTaskSummaryEntity(99L, 50L, 1L, "PICKUP", "NEW", null, 5L, "Old Agent", 101L);
        when(summaryRepository.findByTaskId(1L)).thenReturn(Optional.of(existingSummary));
        ArgumentCaptor<AgentInfoRequestEvent> requestCaptor = ArgumentCaptor.forClass(AgentInfoRequestEvent.class);

        // When
        listener.onTaskAssignedToAgent(event);

        // Then
        verify(summaryRepository).save(existingSummary);
        assertThat(existingSummary.getAgentId()).isEqualTo(7L);

        verify(events).publishEvent(requestCaptor.capture());
        AgentInfoRequestEvent request = requestCaptor.getValue();
        assertThat(request.agentId()).isEqualTo(7L);
        TaskAssignedToAgentEvent tEvent= (TaskAssignedToAgentEvent) request.originalEvent();
        assertThat(tEvent.taskId()).isEqualTo(1L);
    }

    @Test
    void onTeamAgentInfoProvided_shouldUpdateAgentNameInSummary() {
        // Given
        AgentInfoResponseEvent event = new AgentInfoResponseEvent("agentx", AgentState.ACTIVE, 10L, "999","egentx-uid", LocalDate.now(), LocalDate.now(), AgentDesignation.FLEET_AGENT, new TaskCreatedEvent(10L, 20L, "test","test","test", LocalDateTime.now(), "test",30L, "test",40L, 50L, 60L));
        TeamTaskSummaryEntity existingSummary = new TeamTaskSummaryEntity(99L, 50L, 1L, "PICKUP", "NEW", null, 7L, null, 101L);
        when(summaryRepository.findByTaskId(10L)).thenReturn(Optional.of(existingSummary));

        // When
        listener.onTeamAgentInfoProvided(event);

        // Then
        verify(summaryRepository).save(existingSummary);
        assertThat(existingSummary.getAgentName()).isEqualTo("agentx");
    }

    @Test
    void onTaskStatusChanged_shouldUpdateStatusInSummary() {
        // Given
        TaskStatusChangedEvent event = new TaskStatusChangedEvent(1L, 101L, "NEW", "STARTED");
        TeamTaskSummaryEntity existingSummary = new TeamTaskSummaryEntity(99L, 50L, 1L, "PICKUP", "NEW", null, 5L, "Agent Name", 101L);
        when(summaryRepository.findByTaskId(1L)).thenReturn(Optional.of(existingSummary));

        // When
        listener.onTaskStatusChanged(event);

        // Then
        verify(summaryRepository).save(existingSummary);
        assertThat(existingSummary.getTaskStatus()).isEqualTo("STARTED");
    }

    @Test
    void onTaskDeleted_shouldDeleteSummary() {
        // Given
        TaskDeletedEvent event = new TaskDeletedEvent(1L);
        TeamTaskSummaryEntity summaryToDelete = new TeamTaskSummaryEntity();
        summaryToDelete.setId(99L);
        when(summaryRepository.findByTaskId(1L)).thenReturn(Optional.of(summaryToDelete));

        // When
        listener.onTaskDeleted(event);

        // Then
        verify(summaryRepository).deleteById(99L);
    }
}