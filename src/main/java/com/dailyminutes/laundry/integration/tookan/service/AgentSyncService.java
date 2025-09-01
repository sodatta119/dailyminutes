/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.integration.tookan.service;


import com.dailyminutes.laundry.agent.domain.event.AgentSyncEvent;
import com.dailyminutes.laundry.integration.tookan.client.TookanSyncClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * The type Agent sync service.
 */
@Service
@RequiredArgsConstructor
public class AgentSyncService {

    private final TookanSyncClient tookanClient;
    private final ApplicationEventPublisher events;

    /**
     * Sync agents.
     */
    @Transactional
    public void syncAgents() {
        var tookanAgents = tookanClient.listAgents(); // calls /v2/get_all_fleets

        var payloads = tookanAgents.stream()
                .map(a -> new AgentSyncEvent.AgentSyncPayload(
                        String.valueOf(a.fleetId()),
                        a.name(),
                        a.email(),
                        a.phone(),
                        a.teamId(),
                        a.latitude(),
                        a.longitude(),
                        a.isActive(),
                        a.isAvailable(),
                        a.fleetThumbImage(),
                        LocalDateTime.now()
                ))
                .collect(Collectors.toList());

        events.publishEvent(new AgentSyncEvent(payloads));
    }
}
