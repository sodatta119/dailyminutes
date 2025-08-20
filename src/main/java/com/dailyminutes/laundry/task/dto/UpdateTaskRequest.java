/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.dto;

import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * The type Update task request.
 */
public record UpdateTaskRequest(
        @NotNull Long id,
        @NotBlank String name,
        String description,
        @NotNull TaskType type,
        @NotNull LocalDateTime taskStartTime,
        LocalDateTime taskUpdatedTime,
        LocalDateTime taskCompletedTime,
        @NotNull TaskStatus status,
        Long teamId,
        Long agentId,
        String sourceAddress,
        Long sourceGeofenceId,
        String destinationAddress,
        Long destinationGeofenceId,
        String taskComment,
        @NotNull Long orderId
) {
}