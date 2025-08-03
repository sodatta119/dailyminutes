/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.team.domain.event;

public record TeamInfoRequestEvent(
        Long agentId, // Used as a correlation ID to track the request
        Long teamId
) {
}