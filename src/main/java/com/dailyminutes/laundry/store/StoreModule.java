/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(id = "laundry.store", allowedDependencies = {"laundry.common::events"})
public class StoreModule {
}
