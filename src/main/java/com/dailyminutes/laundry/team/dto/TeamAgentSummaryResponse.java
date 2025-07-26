/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.dto;

public record TeamAgentSummaryResponse(
        Long id,
        Long teamId,
        Long agentId,
        String agentName,
        String agentPhoneNumber,
        String agentDesignation,
        String agentState
) {}