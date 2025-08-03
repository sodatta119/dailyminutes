/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.invoice;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(id = "laundry.invoice", allowedDependencies = {"laundry.common::events", "laundry.customer::events", "laundry.order::events", "laundry.payment::events"})
public class InvoiceModule {
}
