/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 19/08/25
 */
package com.dailyminutes.laundry.store.domain.event;

public record GeofenceRemovedFromStoreEvent(
        Long storeId,
        Long geofenceId
) {
}
