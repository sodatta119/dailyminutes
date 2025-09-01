/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * The type Create customer request.
 */
public record CreateCustomerRequest(
        @NotBlank(message = "Subscriber ID cannot be blank")
        String subscriberId,
        @NotBlank(message = "Phone number cannot be blank")
        //@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
        String phoneNumber,
        @NotBlank(message = "Customer name cannot be blank")
        String name,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,
        String timeZone,
        LocalDateTime subscribedAt
) {
}
