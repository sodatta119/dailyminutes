/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.dto;

public record TaskTeamSummaryResponse(
        Long id,
        Long taskId,
        Long teamId,
        String teamName,
        String teamDescription,
        String teamRole
) {}