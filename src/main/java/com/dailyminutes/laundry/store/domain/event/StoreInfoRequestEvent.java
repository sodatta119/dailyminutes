/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.store.domain.event;

// Event fired by other modules when they need store details
public record StoreInfoRequestEvent(
        Long storeId,
        CatalogItemAddedToStoreEvent originalEvent
) {}