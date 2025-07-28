/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.order.domain.event;

import java.math.BigDecimal;

// This is a simple data carrier for the event
public record OrderItemInfo(
        Long orderItemId,
        Long catalogId,
        BigDecimal quantity,
        BigDecimal itemPriceAtOrder
) {}