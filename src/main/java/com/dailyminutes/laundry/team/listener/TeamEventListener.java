/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.team.listener;

import com.dailyminutes.laundry.team.domain.event.TeamInfoRequestEvent;
import com.dailyminutes.laundry.team.domain.event.TeamInfoResponseEvent;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamEventListener {

    private final TeamRepository teamRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onTeamInfoRequested(TeamInfoRequestEvent event) {
        teamRepository.findById(event.teamId()).ifPresent(team -> {
            events.publishEvent(new TeamInfoResponseEvent(
                    event.agentId(),
                    team.getId(),
                    team.getName(),
                    team.getDescription()
            ));
        });
    }
}