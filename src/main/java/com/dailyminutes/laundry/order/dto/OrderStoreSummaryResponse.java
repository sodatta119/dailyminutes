/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.dto;

public record OrderStoreSummaryResponse(
        Long id,
        Long orderId,
        Long storeId,
        String storeName,
        String storeAddress,
        String storeContactNumber,
        String storeEmail
) {
}