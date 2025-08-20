/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.dto;

/**
 * The type Payment customer summary response.
 */
public record PaymentCustomerSummaryResponse(
        Long id,
        Long paymentId,
        Long customerId,
        String customerName,
        String customerPhoneNumber,
        String customerEmail
) {
}