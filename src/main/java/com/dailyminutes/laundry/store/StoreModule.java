/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(id = "laundry.store", allowedDependencies = {"laundry.common::events","laundry.geofence::events","laundry.order::events"})
public class StoreModule {
}
