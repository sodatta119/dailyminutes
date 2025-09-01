/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager;

import org.springframework.modulith.ApplicationModule;

/**
 * The type Manager module.
 */
@ApplicationModule(id = "laundry.manager", allowedDependencies = {"laundry.team::events"})
public class ManagerModule {
}
