/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 01/09/25
 */
package com.dailyminutes.laundry.customer.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.LocalDateTime;

/**
 * The type Customer sync event.
 */
@DomainEvent
public record CustomerSyncEvent(CustomerSyncPayload payload) implements CallerEvent {

    /**
     * The type Customer sync payload.
     */
    public record CustomerSyncPayload(
            String externalId,   // ManyChat subscriberId
            String name,
            String email,
            String phone,
            String timeZone,
            LocalDateTime subscribedAt,
            String addressLine,
            String latitude,
            String longitude,
            String geofenceId
    ) {}
}
