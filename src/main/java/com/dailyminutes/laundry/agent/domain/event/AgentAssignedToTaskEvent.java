/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.domain.event;


import com.dailyminutes.laundry.common.exception.EntityPersistenceException;

/**
 * Event fired when an Agent is assigned to or unassigned to a Task.
 * This can be a specific event for team changes, or part of AgentUpdatedEvent.
 * Keeping it separate for clarity in event handling.
 */
public record AgentAssignedToTaskEvent(
        Long agentId,
        Long fromTaskId,
        Long toTaskId
) {
    public AgentAssignedToTaskEvent {
        if (agentId == null) throw new EntityPersistenceException("Agent ID cannot be null");
        if (fromTaskId == null && toTaskId==null) throw new EntityPersistenceException("Task ID cannot be null");
    }
}

