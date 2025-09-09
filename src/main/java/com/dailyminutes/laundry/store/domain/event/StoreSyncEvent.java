/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.store.domain.event;

import java.util.List;

/**
 * The type Team sync event.
 */
public record StoreSyncEvent(List<StoreSyncPayload> stores) {

    /**
     * The type Team sync payload.
     */
    public record StoreSyncPayload(
            String externalId,
            String name,
            String address,
            String contact,
            String email,
            boolean active,
            String latitude,
            String longitude,
            List<Long> serviceGeofences
    )  {}
}
