/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team;

import org.springframework.modulith.ApplicationModule;

/**
 * The type Team module.
 */
@ApplicationModule(id = "laundry.team", allowedDependencies = {"laundry.agent::events","laundry.task::events"})
public class TeamModule {
}
