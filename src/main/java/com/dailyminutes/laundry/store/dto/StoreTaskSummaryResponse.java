/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.dto;

import java.time.LocalDateTime;

public record StoreTaskSummaryResponse(
        Long id,
        Long storeId,
        Long taskId,
        String taskType,
        String taskStatus,
        LocalDateTime taskStartTime,
        Long agentId,
        String agentName,
        Long orderId
) {}