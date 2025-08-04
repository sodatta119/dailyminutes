/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(id = "laundry.order", allowedDependencies = {"laundry.common::events", "laundry.customer::events", "laundry.payment::events", "laundry.invoice::events", "laundry.task::events"})
public class OrderModule {
}
