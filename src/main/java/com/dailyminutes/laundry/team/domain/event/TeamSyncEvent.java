/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.team.domain.event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Team sync event.
 */
public record TeamSyncEvent(List<TeamSyncPayload> teams) {

    /**
     * The type Team sync payload.
     */
    public record TeamSyncPayload(
            String externalId,    // Tookan’s teamId
            String name,          // Tookan’s teamName
            String description,   // optional if Tookan has description
            boolean active,
            LocalDateTime syncedAt
    ) {}
}
