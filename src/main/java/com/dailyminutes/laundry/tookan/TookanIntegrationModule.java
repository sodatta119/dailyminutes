/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 24/08/25
 */
package com.dailyminutes.laundry.tookan;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        allowedDependencies = {"laundry.order", "laundry.customer"}
)
public class TookanIntegrationModule {
}