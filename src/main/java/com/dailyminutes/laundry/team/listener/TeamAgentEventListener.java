/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 21/08/25
 */
package com.dailyminutes.laundry.team.listener;

import com.dailyminutes.laundry.agent.domain.event.AgentCreatedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentDeletedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentSyncedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentUpdatedEvent;
import com.dailyminutes.laundry.team.domain.model.TeamAgentSummaryEntity;
import com.dailyminutes.laundry.team.repository.TeamAgentSummaryRepository;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Team agent event listener.
 */
@Component
@RequiredArgsConstructor
public class TeamAgentEventListener {

    private final TeamAgentSummaryRepository summaryRepository;
    private final TeamRepository teamRepository;

    /**
     * On agent created.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onAgentCreated(AgentCreatedEvent event) {
        if (event.teamId() != null) {
            TeamAgentSummaryEntity summary = new TeamAgentSummaryEntity(
                    null,
                    event.teamId(),
                    event.agentId(),
                    event.name(),
                    event.phoneNumber(),
                    event.designation(),
                    event.state()
            );
            summaryRepository.save(summary);
        }
    }

    /**
     * On agent synced.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onAgentSynced(AgentSyncedEvent event) {
        if (event.externalTeamId() != null) {
            teamRepository.findByExternalId(event.externalTeamId().toString())
                    .ifPresent(team -> {
                        // Check if summary already exists for this agent+team
                        var existing = summaryRepository.findByTeamIdAndAgentId(team.getId(), event.agentId());

                        if (existing.isPresent()) {
                            var summary = existing.get();
                            summary.setAgentName(event.name());
                            summary.setAgentPhoneNumber(event.phoneNumber());
                            summary.setAgentDesignation(event.designation());
                            summary.setAgentState(event.state());
                            summaryRepository.save(summary);
                        } else {
                            TeamAgentSummaryEntity summary = new TeamAgentSummaryEntity(
                                    null,
                                    team.getId(),
                                    event.agentId(),
                                    event.name(),
                                    event.phoneNumber(),
                                    event.designation(),
                                    event.state()
                            );
                            summaryRepository.save(summary);
                        }
                    });
        }
    }


    /**
     * On agent updated.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onAgentUpdated(AgentUpdatedEvent event) {
        summaryRepository.findByAgentId(event.agentId()).ifPresentOrElse(
                summary -> {
                    // Scenario 1: Agent's team has changed.
                    if (!summary.getTeamId().equals(event.teamId())) {
                        summaryRepository.deleteById(summary.getId());
                        // If assigned to a new team, create a new summary record.
                        if (event.teamId() != null) {
                            createNewSummaryFromEvent(event);
                        }
                    } else {
                        // Scenario 2: Agent's details updated, but team is the same.
                        summary.setAgentName(event.name());
                        summary.setAgentPhoneNumber(event.phoneNumber());
                        summary.setAgentDesignation(event.designation());
                        summary.setAgentState(event.state());
                        summaryRepository.save(summary);
                    }
                },
                () -> {
                    // Scenario 3: Agent was not in a summary but is now assigned to a team.
                    if (event.teamId() != null) {
                        createNewSummaryFromEvent(event);
                    }
                }
        );
    }

    /**
     * On agent deleted.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onAgentDeleted(AgentDeletedEvent event) {
        summaryRepository.deleteByAgentId(event.agentId());
    }

    private void createNewSummaryFromEvent(AgentUpdatedEvent event) {
        TeamAgentSummaryEntity summary = new TeamAgentSummaryEntity(
                null,
                event.teamId(),
                event.agentId(),
                event.name(),
                event.phoneNumber(),
                event.designation(),
                event.state()
        );
        summaryRepository.save(summary);
    }
}