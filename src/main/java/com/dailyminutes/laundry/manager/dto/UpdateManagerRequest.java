/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The type Update manager request.
 */
public record UpdateManagerRequest(
        @NotNull(message = "Manager ID cannot be null")
        Long id,
        @NotBlank(message = "Manager name cannot be blank")
        String name,
        @NotBlank(message = "Contact information cannot be blank")
        String contact
) {
}