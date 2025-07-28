/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.store.domain.event;

import com.dailyminutes.laundry.store.spi.CallerEvent;

// Event fired by other modules when they need store details
public record StoreInfoRequestEvent(
        Long storeId,
        CallerEvent originalEvent
) {}