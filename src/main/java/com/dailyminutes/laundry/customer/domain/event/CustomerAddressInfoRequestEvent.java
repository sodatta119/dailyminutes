/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/07/25
 */
package com.dailyminutes.laundry.customer.domain.event;


import com.dailyminutes.laundry.common.events.CallerEvent;

/**
 * The type Customer address info request event.
 */
public record CustomerAddressInfoRequestEvent(
        Long customerId,
        CallerEvent originalEvent // The invoiceId
) {
}