/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.domain.event;


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
    /**
     * Instantiates a new Agent assigned to task event.
     *
     * @param agentId    the agent id
     * @param fromTaskId the from task id
     * @param toTaskId   the to task id
     */
    public AgentAssignedToTaskEvent {
        if (agentId == null) throw new IllegalArgumentException("Agent ID cannot be null");
        if (fromTaskId == null && toTaskId == null) throw new IllegalArgumentException("Task ID cannot be null");
    }
}

