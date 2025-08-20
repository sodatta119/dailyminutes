/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

public record TaskAssignedToAgentEvent(
        Long taskId,
        Long agentId
) implements CallerEvent {
}