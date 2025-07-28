/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.agent.listeners;

import com.dailyminutes.laundry.agent.domain.event.AgentAssignedToTeamEvent;
import com.dailyminutes.laundry.agent.domain.model.AgentTeamSummaryEntity;
import com.dailyminutes.laundry.agent.repository.AgentTeamSummaryRepository;
import com.dailyminutes.laundry.team.domain.event.TeamInfoRequestEvent;
import com.dailyminutes.laundry.team.domain.event.TeamInfoResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgentTeamEventListener {

    private final AgentTeamSummaryRepository agentTeamSummaryRepository;
    private final ApplicationEventPublisher events;

    /**
     * Step 1: Reacts to the local agent event and requests more info.
     */
    @ApplicationModuleListener
    public void onAgentAssignedToTeam(AgentAssignedToTeamEvent event) {
        // First, always remove the old summary.
        agentTeamSummaryRepository.findByAgentId(event.agentId()).forEach(summary ->
                agentTeamSummaryRepository.deleteById(summary.getId())
        );

        // If assigned to a new team, publish an event to ask for that team's details.
        if (event.teamId() != null) {
            events.publishEvent(new TeamInfoRequestEvent(event.agentId(), event.teamId()));
        }
    }

    /**
     * Step 2: Reacts to the event from the team module and populates the summary.
     */
    @ApplicationModuleListener
    public void onTeamInfoProvided(TeamInfoResponseEvent event) {
        AgentTeamSummaryEntity newSummary = new AgentTeamSummaryEntity(
                null,
                event.agentId(),
                event.teamId(),
                event.teamName(),
                event.teamDescription()
        );
        agentTeamSummaryRepository.save(newSummary);
    }
}