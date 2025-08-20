/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.domain.event;


/**
 * The type Customer updated event.
 */
public record CustomerUpdatedEvent(
        Long customerId,
        String subscriberId,
        String phoneNumber,
        String name,
        String email
) {
}