/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 08/09/25
 */
package com.dailyminutes.laundry.geofence.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

public record GeofenceLookupRequestEvent(
        Long externalGeofenceId,
        CallerEvent originalEvent
) implements CallerEvent {}
