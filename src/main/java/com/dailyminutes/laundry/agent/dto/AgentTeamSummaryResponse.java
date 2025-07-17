/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.dto;

/**
 * DTO for AgentTeamSummary response.
 */
public record AgentTeamSummaryResponse(
        Long id,
        Long teamId,
        Long agentId,
        String teamName,
        String teamDescription
) {}