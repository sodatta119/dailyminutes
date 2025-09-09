/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * The type Create store request.
 */
public record CreateStoreRequest(
        @NotBlank(message = "Store name cannot be blank")
        String name,
        @NotBlank(message = "Address cannot be blank")
        String address,
        String contactNumber,
        @Email(message = "Email should be valid")
        String email,
        Long managerId,
        @NotBlank(message = "Latitude cannot be blank")
        String latitude,
        @NotBlank(message = "Longitude cannot be blank")
        String longitude
) {
}