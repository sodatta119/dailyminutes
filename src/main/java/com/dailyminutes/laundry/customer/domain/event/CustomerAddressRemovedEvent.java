/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.domain.event;

/**
 * The type Customer address removed event.
 */
public record CustomerAddressRemovedEvent(Long addressId, Long customerId) {
}