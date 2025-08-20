/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 04/08/25
 */
package com.dailyminutes.laundry.geofence.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

public record GeofenceInfoResponseEvent(
        Long geofenceId,
        String geofenceName,
        String geofenceType,
        boolean active,
        String polygonCoordinates,
        CallerEvent originalEvent
) {}