/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.dto;

/**
 * The type Task agent summary response.
 */
public record TaskAgentSummaryResponse(
        Long id,
        Long taskId,
        Long agentId,
        String agentName,
        String agentPhoneNumber,
        String agentDesignation,
        String agentState
) {
}