/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 20/08/25
 */
package com.dailyminutes.laundry.agent.domain.event;

/**
 * The type Agent info request event.
 */
public record AgentRefreshRequestEvent(Long agentId, Long externalId) {
}
