/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.order.domain.event;


import com.dailyminutes.laundry.common.events.CallerEvent;

public record OrderInfoResponseEvent(
        Long orderId,
        Long customerId,
        CallerEvent originalEvent
) {}