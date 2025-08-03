/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.order.domain.event;


import com.dailyminutes.laundry.common.events.CallerEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderInfoResponseEvent(
        Long orderId,
        Long customerId,
        LocalDateTime orderDate,
        String status,
        BigDecimal totalAmount,
        CallerEvent originalEvent
) {
}