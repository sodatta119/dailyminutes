/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.dto;

/**
 * The type Task team summary response.
 */
public record TaskTeamSummaryResponse(
        Long id,
        Long taskId,
        Long teamId,
        String teamName,
        String teamDescription,
        String teamRole
) {
}