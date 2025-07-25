/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.payment.dto;

public record PaymentCustomerSummaryResponse(
        Long id,
        Long paymentId,
        Long customerId,
        String customerName,
        String customerPhoneNumber,
        String customerEmail
) {}