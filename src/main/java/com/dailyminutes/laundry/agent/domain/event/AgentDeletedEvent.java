/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.domain.event;

/**
 * Event fired when an Agent is deleted.
 * Contains the ID of the deleted agent.
 */
public record AgentDeletedEvent(
        Long agentId
) {
    /**
     * Instantiates a new Agent deleted event.
     *
     * @param agentId the agent id
     */
    public AgentDeletedEvent {
        if (agentId == null) throw new IllegalArgumentException("Agent ID cannot be null");
    }
}
