/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.domain.event;

/**
 * The type Store updated event.
 */
public record StoreUpdatedEvent(
        Long storeId,
        String newName,
        Long newManagerId
) {
}