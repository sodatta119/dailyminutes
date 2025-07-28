/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.store.domain.event;

import com.dailyminutes.laundry.store.spi.CallerEvent;

public record GeofenceAssignedToStoreEvent(
        Long storeId,
        Long geofenceId
) implements CallerEvent {}