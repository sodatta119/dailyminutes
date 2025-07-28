/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.team.domain.event;

public record TeamInfoResponseEvent(
        Long agentId, // Correlation ID
        Long teamId,
        String teamName,
        String teamDescription
) {}