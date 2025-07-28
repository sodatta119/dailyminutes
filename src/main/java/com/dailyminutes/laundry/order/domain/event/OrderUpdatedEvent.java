/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderUpdatedEvent(
        Long orderId,
        LocalDateTime newOrderDate,
        String newStatus,
        BigDecimal newTotalAmount
) {}