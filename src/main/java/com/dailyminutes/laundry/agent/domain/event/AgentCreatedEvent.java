/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.domain.event;

import java.time.LocalDate;

/**
 * The type Agent created event.
 */
public record AgentCreatedEvent(
        Long agentId,
        String name,
        String state,
        Long teamId,
        String phoneNumber,
        String uniqueId,
        LocalDate dateOfJoining,
        String designation
) {
    /**
     * Instantiates a new Agent created event.
     *
     * @param agentId       the agent id
     * @param name          the name
     * @param state         the state
     * @param teamId        the team id
     * @param phoneNumber   the phone number
     * @param uniqueId      the unique id
     * @param dateOfJoining the date of joining
     * @param designation   the designation
     */
    public AgentCreatedEvent {
        if (agentId == null) throw new IllegalArgumentException("Agent ID cannot be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Agent name cannot be null or blank");
        if (state == null) throw new IllegalArgumentException("Agent state cannot be null");
        if (phoneNumber == null || phoneNumber.isBlank())
            throw new IllegalArgumentException("Agent phone number cannot be null or blank");
//        if (uniqueId == null || uniqueId.isBlank())
//            throw new IllegalArgumentException("Agent unique ID cannot be null or blank");
//        if (dateOfJoining == null) throw new IllegalArgumentException("Date of joining cannot be null");
//        if (designation == null) throw new IllegalArgumentException("Agent designation cannot be null");
    }
}
