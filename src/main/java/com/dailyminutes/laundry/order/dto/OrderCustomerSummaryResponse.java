/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.order.dto;

/**
 * The type Order customer summary response.
 */
public record OrderCustomerSummaryResponse(
        Long id,
        Long orderId,
        Long customerId,
        String customerName,
        String customerPhoneNumber,
        String customerEmail
) {
}