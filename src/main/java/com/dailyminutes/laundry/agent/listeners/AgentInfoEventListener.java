/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 20/08/25
 */
package com.dailyminutes.laundry.agent.listeners;

import com.dailyminutes.laundry.agent.domain.event.AgentInfoRequestEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentInfoResponseEvent;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import com.dailyminutes.laundry.team.domain.event.TeamAgentInfoRequestEvent;
import com.dailyminutes.laundry.team.domain.event.TeamAgentInfoResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgentInfoEventListener {

    private final AgentRepository agentRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onAgentInfoRequested(AgentInfoRequestEvent event) {
        agentRepository.findById(event.agentId()).ifPresent(agent -> {
            events.publishEvent(new AgentInfoResponseEvent(
                    agent.getName(),
                    agent.getState(),
                    agent.getTeamId(),
                    agent.getPhoneNumber(),
                    agent.getUniqueId(),
                    agent.getJoiningDate(),
                    agent.getTerminationDate(),
                    agent.getDesignation(),
                    event.originalEvent() // Pass the storeId back
            ));
        });
    }

    @ApplicationModuleListener
    public void onTeamAgentInfoRequested(TeamAgentInfoRequestEvent event) {
        agentRepository.findById(event.agentId()).ifPresent(agent -> {
            events.publishEvent(new TeamAgentInfoResponseEvent(
                    agent.getId(),
                    event.taskId(),
                    agent.getName(),
                    agent.getUniqueId()
            ));
        });
    }
}
