/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.domain.event;

public record TaskAssignedToAgentEvent(
        Long taskId,
        Long agentId
) {}