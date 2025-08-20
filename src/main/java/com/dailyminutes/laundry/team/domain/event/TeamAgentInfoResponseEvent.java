/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.domain.event;

public record TeamAgentInfoResponseEvent(
        Long agentId,
        Long taskId,
        String agentName,
        String agentUniqueId
) {
}