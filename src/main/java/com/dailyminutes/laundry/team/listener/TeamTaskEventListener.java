/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 20/08/25
 */
package com.dailyminutes.laundry.team.listener;

import com.dailyminutes.laundry.task.domain.event.TaskAssignedToAgentEvent;
import com.dailyminutes.laundry.task.domain.event.TaskCreatedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskDeletedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskStatusChangedEvent;
import com.dailyminutes.laundry.team.domain.event.TeamAgentInfoRequestEvent;
import com.dailyminutes.laundry.team.domain.event.TeamAgentInfoResponseEvent;
import com.dailyminutes.laundry.team.domain.model.TeamTaskSummaryEntity;
import com.dailyminutes.laundry.team.repository.TeamTaskSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamTaskEventListener {

    private final TeamTaskSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onTaskCreated(TaskCreatedEvent event) {
        if (event.teamId() == null) {
            return; // Only summarize tasks that are assigned to a team
        }
        TeamTaskSummaryEntity summary = new TeamTaskSummaryEntity(
                null,
                event.teamId(),
                event.taskId(),
                event.type(), // Assuming type is String in event
                event.status(), // Assuming status is String in event
                event.taskStartTime(),
                event.agentId(),
                null, // Agent name is currently unknown
                event.orderId()
        );
        summaryRepository.save(summary);

        // If an agent is assigned at creation, ask for their name
        if (event.agentId() != null) {
            events.publishEvent(new TeamAgentInfoRequestEvent(event.agentId(), event.taskId()));
        }
    }

    @ApplicationModuleListener
    public void onTaskAssignedToAgent(TaskAssignedToAgentEvent event) {
        summaryRepository.findByTaskId(event.taskId()).ifPresent(summary -> {
            summary.setAgentId(event.agentId());
            summaryRepository.save(summary);
            // Ask for the new agent's name
            events.publishEvent(new TeamAgentInfoRequestEvent(event.agentId(), event.taskId()));
        });
    }

    @ApplicationModuleListener
    public void onTeamAgentInfoProvided(TeamAgentInfoResponseEvent event) {
            summaryRepository.findByTaskId(event.taskId()).ifPresent(summary -> {
                summary.setAgentName(event.agentName());
                summaryRepository.save(summary);
            });
    }

    @ApplicationModuleListener
    public void onTaskStatusChanged(TaskStatusChangedEvent event) {
        summaryRepository.findByTaskId(event.taskId()).ifPresent(summary -> {
            summary.setTaskStatus(event.newStatus());
            summaryRepository.save(summary);
        });
    }

    @ApplicationModuleListener
    public void onTaskDeleted(TaskDeletedEvent event) {
        summaryRepository.findByTaskId(event.taskId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}