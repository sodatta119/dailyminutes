/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 25/07/25
 */
package com.dailyminutes.laundry.customer;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(id = "laundry.customer",allowedDependencies = {"laundry.order::events", "laundry.common::events"})
public class CustomerModule {
}
