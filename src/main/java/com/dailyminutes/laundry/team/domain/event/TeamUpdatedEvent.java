/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.domain.event;

public record TeamUpdatedEvent(
        Long teamId,
        String newName
) {}