/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 07/09/25
 */
package com.dailyminutes.laundry.integration.tookan.event;

import com.dailyminutes.laundry.common.events.CallerEvent;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record CustomerGeofenceRequestEvent(String subscriptionId, CallerEvent originalEvent) { }

