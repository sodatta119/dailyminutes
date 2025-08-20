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
 * The type Create task request.
 */
public record CreateTaskRequest(
        @NotBlank String name,
        String description,
        @NotNull TaskType type,
        @NotNull LocalDateTime taskStartTime,
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