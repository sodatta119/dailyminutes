/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog;

import org.springframework.modulith.ApplicationModule;

/**
 * The type Catalog module.
 */
@ApplicationModule(allowedDependencies = {"laundry.order::events", "laundry.store::events"})
public class CatalogModule {
}
