/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.dto;

import java.time.LocalDateTime;

/**
 * The type Order task summary response.
 */
public record OrderTaskSummaryResponse(
        Long id,
        Long orderId,
        Long taskId,
        String taskType,
        String taskStatus,
        LocalDateTime taskStartTime,
        Long agentId,
        String agentName
) {
}