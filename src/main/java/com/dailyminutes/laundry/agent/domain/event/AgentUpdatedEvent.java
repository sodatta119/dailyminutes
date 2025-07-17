/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.domain.event;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.common.exception.EntityPersistenceException;

import java.time.LocalDate;

/**
 * Event fired when an existing Agent's details are updated.
 * Contains the updated essential details of the agent.
 */
public record AgentUpdatedEvent(
        Long agentId,
        String name,
        AgentState state,
        Long teamId, // Can be null if not assigned to a team
        String phoneNumber,
        String uniqueId,
        LocalDate dateOfJoining,
        LocalDate dateOfLeaving, // Can be null if agent is still active
        AgentDesignation designation
) {
    public AgentUpdatedEvent {
        if (agentId == null) throw new EntityPersistenceException("Agent ID cannot be null");
        if (name == null || name.isBlank()) throw new EntityPersistenceException("Agent name cannot be null or blank");
        if (state == null) throw new EntityPersistenceException("Agent state cannot be null");
        if (phoneNumber == null || phoneNumber.isBlank()) throw new EntityPersistenceException("Agent phone number cannot be null or blank");
        if (uniqueId == null || uniqueId.isBlank()) throw new EntityPersistenceException("Agent unique ID cannot be null or blank");
        if (dateOfJoining == null) throw new EntityPersistenceException("Date of joining cannot be null");
        if (designation == null) throw new EntityPersistenceException("Agent designation cannot be null");
    }
}
