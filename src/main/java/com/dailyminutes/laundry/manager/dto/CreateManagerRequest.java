/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateManagerRequest(
        @NotBlank(message = "Manager name cannot be blank")
        String name,
        @NotBlank(message = "Contact information cannot be blank")
        String contact
) {
}