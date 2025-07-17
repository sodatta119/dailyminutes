/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.dto;

import java.time.LocalDateTime;

/**
 * DTO for AgentTaskSummary response.
 */
public record AgentTaskSummaryResponse(
        Long taskId,
        Long agentId,
        String taskName,
        String taskType,
        String taskStatus,
        LocalDateTime taskStartTime,
        String sourceAddress,
        String destinationAddress,
        Long orderId
) {}