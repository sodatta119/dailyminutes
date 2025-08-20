/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 20/08/25
 */
package com.dailyminutes.laundry.agent.domain.event;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.common.events.CallerEvent;

import java.time.LocalDate;

/**
 * The type Agent info response event.
 */
public record AgentInfoResponseEvent(
        String name,
        AgentState state,
        Long teamId,
        String phoneNumber,
        String uniqueId,
        LocalDate joiningDate,
        LocalDate terminationDate,
        AgentDesignation designation,
        CallerEvent originalEvent) {
}
