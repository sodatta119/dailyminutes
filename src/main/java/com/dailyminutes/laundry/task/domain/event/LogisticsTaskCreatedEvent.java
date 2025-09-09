/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/09/25
 */
package com.dailyminutes.laundry.task.domain.event;

import java.time.LocalDateTime;

public record LogisticsTaskCreatedEvent(
        Long orderId,          // internal orderId (correlation key)
        String logisticsTaskId,   // Logistic’s job_id
        String logisticsOrderId,  // optional: if Logistic allows order ref
        LocalDateTime createdAt
) {}