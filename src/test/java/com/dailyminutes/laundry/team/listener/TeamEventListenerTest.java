package com.dailyminutes.laundry.team.listener;

import com.dailyminutes.laundry.team.domain.event.TeamInfoRequestEvent;
import com.dailyminutes.laundry.team.domain.event.TeamInfoResponseEvent;
import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.domain.model.TeamRole;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamEventListenerTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private TeamEventListener listener;

    @Test
    void onTeamInfoRequested_shouldFindTeamAndPublishInfo() {
        // Given: A request event for team details
        TeamInfoRequestEvent requestEvent = new TeamInfoRequestEvent(1L, 10L);
        TeamEntity teamEntity = new TeamEntity(10L, "Fleet Team A", "Handles pickups", TeamRole.FLEET);

        when(teamRepository.findById(10L)).thenReturn(Optional.of(teamEntity));
        ArgumentCaptor<TeamInfoResponseEvent> eventCaptor = ArgumentCaptor.forClass(TeamInfoResponseEvent.class);

        // When: The listener handles the request
        listener.onTeamInfoRequested(requestEvent);

        // Then: It should publish a response event with the correct details
        verify(events).publishEvent(eventCaptor.capture());
        TeamInfoResponseEvent responseEvent = eventCaptor.getValue();

        // The agentId (correlation ID) should be carried over
        assertThat(responseEvent.agentId()).isEqualTo(1L);
        assertThat(responseEvent.teamId()).isEqualTo(10L);
        assertThat(responseEvent.teamName()).isEqualTo("Fleet Team A");
        assertThat(responseEvent.teamDescription()).isEqualTo("Handles pickups");
    }
}