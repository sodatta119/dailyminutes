/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.domain.event;

import java.time.LocalDateTime;

public record TaskCreatedEvent(
        Long taskId,
        Long orderId,
        String name,
        String type,
        String status,
        LocalDateTime taskStartTime,
        String sourceAddress,
        String destinationAddress,
        Long agentId,
        Long teamId
) {
}