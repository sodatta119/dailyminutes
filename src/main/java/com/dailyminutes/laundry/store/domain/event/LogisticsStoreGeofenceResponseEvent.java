/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 03/09/25
 */
package com.dailyminutes.laundry.store.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

public record LogisticsStoreGeofenceResponseEvent(
        Long storeId,
        String storeName,
        String storeAddress,
        String storeLatitude,
        String storeLongitude,
        CallerEvent customerEvent,
        CallerEvent orderEvent
) {
}