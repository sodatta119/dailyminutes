/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.customer.domain.event;


import com.dailyminutes.laundry.common.events.CallerEvent;

public record CustomerInfoResponseEvent(
        Long customerId,
        String customerName,
        String customerPhoneNumber,
        String customerEmail,
        CallerEvent originalEvent // The original invoiceId
) implements CallerEvent{}