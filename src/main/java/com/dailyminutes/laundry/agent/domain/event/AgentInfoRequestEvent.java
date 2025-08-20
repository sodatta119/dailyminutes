/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 20/08/25
 */
package com.dailyminutes.laundry.agent.domain.event;

import com.dailyminutes.laundry.common.events.CallerEvent;

/**
 * The type Agent info request event.
 */
public record AgentInfoRequestEvent(Long agentId, CallerEvent originalEvent) {
}
