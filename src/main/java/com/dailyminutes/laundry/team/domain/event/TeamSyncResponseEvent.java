/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.domain.event;

/**
 * The type Team created event.
 */
public record TeamSyncResponseEvent(
        Long teamId,
        String externalId,
        String teamName,
        String teamRole,
        String teamDescription,
        Boolean isDeleted
) {
}