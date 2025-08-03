/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.dto;


public record CustomerResponse(
        Long id,
        String subscriberId,
        String phoneNumber,
        String name,
        String email
) {
}
