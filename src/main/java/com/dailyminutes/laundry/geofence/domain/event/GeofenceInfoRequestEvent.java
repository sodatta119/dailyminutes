/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 04/08/25
 */
package com.dailyminutes.laundry.geofence.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

/**
 * The type Geofence info request event.
 */
public record GeofenceInfoRequestEvent(
        Long geofenceId,
        CallerEvent originalEvent // Will hold the storeId
) {}