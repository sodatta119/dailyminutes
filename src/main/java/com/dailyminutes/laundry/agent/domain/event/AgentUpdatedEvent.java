/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.domain.event;

import java.time.LocalDate;

/**
 * Event fired when an existing Agent's details are updated.
 * Contains the updated essential details of the agent.
 */
public record AgentUpdatedEvent(
        Long agentId,
        String name,
        String state,
        Long teamId, // Can be null if not assigned to a team
        String phoneNumber,
        String uniqueId,
        LocalDate dateOfJoining,
        LocalDate dateOfLeaving, // Can be null if agent is still active
        String designation
) {
    public AgentUpdatedEvent {
        if (agentId == null) throw new IllegalArgumentException("Agent ID cannot be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Agent name cannot be null or blank");
        if (state == null) throw new IllegalArgumentException("Agent state cannot be null");
        if (phoneNumber == null || phoneNumber.isBlank())
            throw new IllegalArgumentException("Agent phone number cannot be null or blank");
        if (uniqueId == null || uniqueId.isBlank())
            throw new IllegalArgumentException("Agent unique ID cannot be null or blank");
        if (dateOfJoining == null) throw new IllegalArgumentException("Date of joining cannot be null");
        if (designation == null) throw new IllegalArgumentException("Agent designation cannot be null");
    }
}
