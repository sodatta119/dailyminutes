/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.domain.event;

import com.dailyminutes.laundry.task.domain.model.TaskStatus;

public record TaskStatusChangedEvent(
        Long taskId,
        Long orderId,
        String oldStatus,
        String newStatus
) {
}