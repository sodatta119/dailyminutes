/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.domain.event;

import com.dailyminutes.laundry.task.domain.model.TaskType;

public record TaskCreatedEvent(
        Long taskId,
        Long orderId,
        TaskType type,
        Long agentId,
        Long teamId
) {}