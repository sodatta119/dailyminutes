/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.store.domain.event;


import com.dailyminutes.laundry.common.events.CallerEvent;

public record CatalogItemAddedToStoreEvent(
        Long storeId,
        Long catalogId
) implements CallerEvent {
}