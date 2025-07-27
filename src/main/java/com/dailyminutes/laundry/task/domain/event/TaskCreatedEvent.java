/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.domain.event;

import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskType;

import java.time.LocalDateTime;

public record TaskCreatedEvent(
        Long taskId,
        Long orderId,
        String name,
        TaskType type,
        TaskStatus status,
        LocalDateTime taskStartTime,
        String sourceAddress,
        String destinationAddress,
        Long agentId,
        Long teamId
) {}