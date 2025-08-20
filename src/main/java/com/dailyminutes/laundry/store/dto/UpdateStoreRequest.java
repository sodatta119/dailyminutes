/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The type Update store request.
 */
public record UpdateStoreRequest(
        @NotNull(message = "Store ID cannot be null")
        Long id,
        @NotBlank(message = "Store name cannot be blank")
        String name,
        @NotBlank(message = "Address cannot be blank")
        String address,
        String contactNumber,
        @Email(message = "Email should be valid")
        String email,
        Long managerId
) {
}