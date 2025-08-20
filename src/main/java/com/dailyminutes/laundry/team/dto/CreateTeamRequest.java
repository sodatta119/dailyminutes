/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.dto;

import com.dailyminutes.laundry.team.domain.model.TeamRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The type Create team request.
 */
public record CreateTeamRequest(
        @NotBlank(message = "Team name cannot be blank")
        String name,
        String description,
        @NotNull(message = "Team role cannot be null")
        TeamRole role
) {
}