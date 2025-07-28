/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(id = "laundry.geofence", allowedDependencies = {"laundry.customer::events","laundry.order::events","laundry.store::events"})
public class GeofenceModule {
}
