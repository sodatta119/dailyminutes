/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 27/07/25
 */
package com.dailyminutes.laundry.agent.listener;

import com.dailyminutes.laundry.agent.domain.model.AgentTaskSummaryEntity;
import com.dailyminutes.laundry.agent.repository.AgentTaskSummaryRepository;
import com.dailyminutes.laundry.task.domain.event.TaskCreatedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskDeletedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Agent task event listener.
 */
@Component
@RequiredArgsConstructor
public class AgentTaskEventListener {

    private final AgentTaskSummaryRepository agentTaskSummaryRepository;

    /**
     * On task created.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onTaskCreated(TaskCreatedEvent event) {
        if (event.agentId() == null) {
            return; // Only create a summary if an agent is assigned
        }

        // All data now comes directly from the event
        AgentTaskSummaryEntity summary = new AgentTaskSummaryEntity(
                null,
                event.taskId(),
                event.agentId(),
                event.name(),
                event.type(),
                event.status(),
                event.taskStartTime(),
                event.sourceAddress(),
                event.destinationAddress(),
                event.orderId()
        );
        agentTaskSummaryRepository.save(summary);
    }

    /**
     * On task status changed.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onTaskStatusChanged(TaskStatusChangedEvent event) {
        agentTaskSummaryRepository.findByTaskId(event.taskId()).ifPresent(summary -> {
            summary.setTaskStatus(event.newStatus());
            agentTaskSummaryRepository.save(summary);
        });
    }

    /**
     * On task deleted.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onTaskDeleted(TaskDeletedEvent event) {
        // Use the new repository method for a clean delete
        agentTaskSummaryRepository.deleteByTaskId(event.taskId());
    }
}