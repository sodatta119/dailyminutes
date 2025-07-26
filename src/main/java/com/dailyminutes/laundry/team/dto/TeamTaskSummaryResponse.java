/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.dto;

import java.time.LocalDateTime;

public record TeamTaskSummaryResponse(
        Long id,
        Long teamId,
        Long taskId,
        String taskType,
        String taskStatus,
        LocalDateTime taskStartTime,
        Long agentId,
        String agentName,
        Long orderId
) {}