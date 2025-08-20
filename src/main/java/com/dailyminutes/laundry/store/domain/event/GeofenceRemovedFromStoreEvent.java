/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 19/08/25
 */
package com.dailyminutes.laundry.store.domain.event;

/**
 * The type Geofence removed from store event.
 */
public record GeofenceRemovedFromStoreEvent(
        Long storeId,
        Long geofenceId
) {
}
