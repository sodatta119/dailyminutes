/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.dto;

import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskType;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String name,
        String description,
        TaskType type,
        LocalDateTime taskStartTime,
        LocalDateTime taskUpdatedTime,
        LocalDateTime taskCompletedTime,
        TaskStatus status,
        Long teamId,
        Long agentId,
        String sourceAddress,
        Long sourceGeofenceId,
        String destinationAddress,
        Long destinationGeofenceId,
        String taskComment,
        Long orderId
) {
}