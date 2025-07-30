/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.store.domain.event;


import com.dailyminutes.laundry.common.events.CallerEvent;

// Event fired by the store module with the requested details
public record StoreInfoResponseEvent(
        Long storeId,
        String storeName,
        String storeAddress,
        CallerEvent originalEvent
) {}