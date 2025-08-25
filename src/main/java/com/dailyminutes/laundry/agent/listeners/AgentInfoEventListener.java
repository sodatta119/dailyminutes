/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 20/08/25
 */
package com.dailyminutes.laundry.agent.listeners;

import com.dailyminutes.laundry.agent.domain.event.AgentInfoRequestEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentInfoResponseEvent;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * The type Agent info event listener.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AgentInfoEventListener {

    private final AgentRepository agentRepository;
    private final ApplicationEventPublisher events;

    /**
     * On agent info requested.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onAgentInfoRequested(AgentInfoRequestEvent event) {
        log.info("Reveived AgentInfoRequestEvent for Agent id"+event.agentId()+". Going to publish AgentInfoResponseEvent.");
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
}
