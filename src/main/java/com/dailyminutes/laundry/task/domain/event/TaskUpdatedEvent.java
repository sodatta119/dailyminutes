/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.domain.event;

import java.time.LocalDateTime;

/**
 * The type Task updated event.
 */
public record TaskUpdatedEvent(
        Long taskId,
        String externalTaskId,   // Tookan’s job_id
        Long orderId,
        Long agentId,            // mapped internal agentId
        String status,           // e.g. ASSIGNED, IN_PROGRESS, COMPLETED
        LocalDateTime updatedAt) {
}