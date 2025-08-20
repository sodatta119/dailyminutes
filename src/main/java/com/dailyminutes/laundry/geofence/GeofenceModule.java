/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence;

import org.springframework.modulith.ApplicationModule;

/**
 * The type Geofence module.
 */
@ApplicationModule(id = "laundry.geofence", allowedDependencies = {"laundry.common::events", "laundry.customer::events", "laundry.order::events"})
public class GeofenceModule {
}
