/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.dto;

/**
 * The type Store agent summary response.
 */
public record StoreAgentSummaryResponse(
        Long id,
        Long storeId,
        Long agentId,
        String agentName,
        String agentPhoneNumber,
        String agentDesignation,
        String agentStatus
) {
}