/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task;

import org.springframework.modulith.ApplicationModule;

/**
 * The type Task module.
 */
@ApplicationModule(id = "laundry.task", allowedDependencies = {"laundry.common::events","laundry.geofence::events"})
public class TaskModule {
}
