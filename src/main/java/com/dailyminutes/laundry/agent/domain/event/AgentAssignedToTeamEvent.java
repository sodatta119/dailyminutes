/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.domain.event;


import com.dailyminutes.laundry.common.exception.EntityPersistenceException;

/**
 * Event fired when an Agent is assigned to a Team.
 * This can be a specific event for team changes, or part of AgentUpdatedEvent.
 * Keeping it separate for clarity in event handling.
 */
public record AgentAssignedToTeamEvent(
        Long agentId,
        Long teamId // Null if unassigned
) {
    public AgentAssignedToTeamEvent {
        if (agentId == null) throw new EntityPersistenceException("Agent ID cannot be null");
        if (teamId == null) throw new EntityPersistenceException("Team ID cannot be null");
    }
}

