/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.store.domain.event;


import com.dailyminutes.laundry.common.events.CallerEvent;

/**
 * The type Geofence assigned to store event.
 */
public record GeofenceAssignedToStoreEvent(
        Long storeId,
        Long geofenceId
) implements CallerEvent {
}