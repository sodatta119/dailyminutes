/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.dto;

import com.dailyminutes.laundry.team.domain.model.TeamRole;

/**
 * The type Team response.
 */
public record TeamResponse(
        Long id,
        String name,
        String description,
        TeamRole role
) {
}