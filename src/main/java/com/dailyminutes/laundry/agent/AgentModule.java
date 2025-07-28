/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 24/07/25
 */
package com.dailyminutes.laundry.agent;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(allowedDependencies = {"laundry.task::events","laundry.team::events"})
public class AgentModule {
}
