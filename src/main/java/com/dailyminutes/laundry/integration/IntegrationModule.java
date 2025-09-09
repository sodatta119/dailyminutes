/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 31/08/25
 */
package com.dailyminutes.laundry.integration;

import org.springframework.modulith.ApplicationModule;

/**
 * The type Integration module.
 */
@ApplicationModule(id = "laundry.integration", allowedDependencies = {"laundry.common::events","laundry.store::events","laundry.team::events","laundry.geofence::events","laundry.agent::events","laundry.customer::events","laundry.order::events"})
public class IntegrationModule {
}
