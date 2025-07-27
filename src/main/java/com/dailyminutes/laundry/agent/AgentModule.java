/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 24/07/25
 */
package com.dailyminutes.laundry.agent;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(allowedDependencies = {"laundry.task::model","laundry.task::event","laundry.team::event","laundry.team::model"})
public class AgentModule {
}
