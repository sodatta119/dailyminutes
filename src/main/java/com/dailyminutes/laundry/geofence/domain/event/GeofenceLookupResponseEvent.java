/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 08/09/25
 */
package com.dailyminutes.laundry.geofence.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

public record GeofenceLookupResponseEvent(
        Long internalGeofenceId,     // null if not found
        Long externalGeofenceId,
        CallerEvent originalEvent
) implements CallerEvent {}