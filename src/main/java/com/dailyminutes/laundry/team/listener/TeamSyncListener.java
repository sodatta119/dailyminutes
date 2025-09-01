/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.team.listener;

import com.dailyminutes.laundry.team.domain.event.TeamSyncEvent;
import com.dailyminutes.laundry.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Team sync listener.
 */
@Component
@RequiredArgsConstructor
class TeamSyncListener {

    private final TeamService teamService; // your domain service

    /**
     * On teams sync event.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onTeamsSyncEvent(TeamSyncEvent event) {
        event.teams().forEach(payload -> {
            teamService.upsertFromSync(payload);
        });
    }
}
