/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 19/08/25
 */
package com.dailyminutes.laundry.store.domain.event;

/**
 * The type Catalog item removed from store event.
 */
public record CatalogItemRemovedFromStoreEvent(
        Long storeId,
        Long catalogId
) {
}
