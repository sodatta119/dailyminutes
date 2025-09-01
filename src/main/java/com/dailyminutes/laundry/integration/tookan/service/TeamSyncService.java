/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.integration.tookan.service;


import com.dailyminutes.laundry.integration.tookan.client.TookanSyncClient;
import com.dailyminutes.laundry.team.domain.event.TeamSyncEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * The type Team sync service.
 */
@Service
@RequiredArgsConstructor
public class TeamSyncService {

    private final TookanSyncClient tookanClient;
    private final ApplicationEventPublisher events;

    /**
     * Sync teams.
     */
    @Transactional
    public void syncTeams() {
        var tookanTeams = tookanClient.listTeams();

        var payloads = tookanTeams.stream()
                .map(t -> new TeamSyncEvent.TeamSyncPayload(
                        String.valueOf(t.team_id()),   // Tookan’s ID as externalId
                        t.team_name(),
                        t.team_name(),
                        true,
                        LocalDateTime.now()
                ))
                .collect(Collectors.toList());

        events.publishEvent(new TeamSyncEvent(payloads));
    }
}
