/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.dto;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;

import java.time.LocalDate;

public record AgentResponse(
        Long id,
        String name,
        AgentState state,
        Long teamId,
        String phoneNumber,
        String uniqueId,
        LocalDate joiningDate,
        LocalDate terminationDate,
        AgentDesignation designation
) {
}