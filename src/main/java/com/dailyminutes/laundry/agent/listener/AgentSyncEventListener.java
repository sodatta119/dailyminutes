/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 31/08/25
 */
package com.dailyminutes.laundry.agent.listener;


import com.dailyminutes.laundry.agent.domain.event.AgentSyncEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentSyncedEvent;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

/**
 * The type Agent sync event listener.
 */
@Component
@RequiredArgsConstructor
public class AgentSyncEventListener {

    private final AgentRepository agentRepository;
    private final ApplicationEventPublisher events;

    /**
     * Handle agent sync.
     *
     * @param event the event
     */
    @EventListener
    public void handleAgentSync(AgentSyncEvent event) {
        for (var payload : event.getPayloads()) {
            Optional<AgentEntity> existing = agentRepository.findByExternalId(payload.externalId());

            AgentEntity entity = existing.orElseGet(AgentEntity::new);

            entity.setExternalId(payload.externalId());
            entity.setName(payload.name());
            //entity.setEmail(payload.email());
            entity.setPhoneNumber(payload.phoneNumber());
            entity.setLatitude(payload.latitude());
            entity.setLongitude(payload.longitude());
            entity.setActive(payload.active());
            entity.setAvailable(payload.available());
            entity.setState(payload.active()==1?AgentState.ACTIVE:AgentState.INACTIVE);
            //entity.setFleetThumbImage(payload.fleetThumbImage());
            entity.setExternalSyncAt(payload.externalSyncAt());
            entity.setUniqueId(payload.externalId());
            entity.setJoiningDate(LocalDate.now());
            entity.setTeamId(payload.teamId());

            AgentEntity savedAgent = agentRepository.save(entity);
            events.publishEvent(new AgentSyncedEvent(savedAgent.getId(), savedAgent.getName(), savedAgent.getState()!=null?savedAgent.getState().name():null, savedAgent.getTeamId(), savedAgent.getPhoneNumber(), savedAgent.getUniqueId(), savedAgent.getJoiningDate(), savedAgent.getDesignation()!=null?savedAgent.getDesignation().name():null));
        }
    }
}
