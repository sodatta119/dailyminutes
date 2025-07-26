package com.dailyminutes.laundry.team.service;

import com.dailyminutes.laundry.team.domain.event.TeamCreatedEvent;
import com.dailyminutes.laundry.team.domain.event.TeamDeletedEvent;
import com.dailyminutes.laundry.team.domain.event.TeamUpdatedEvent;
import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.domain.model.TeamRole;
import com.dailyminutes.laundry.team.dto.CreateTeamRequest;
import com.dailyminutes.laundry.team.dto.UpdateTeamRequest;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private TeamService teamService;

    @Test
    void createTeam_shouldCreateAndPublishEvent() {
        CreateTeamRequest request = new CreateTeamRequest("Test Team", "desc", TeamRole.OPS);
        TeamEntity team = new TeamEntity(1L, "Test Team", "desc", TeamRole.OPS);
        when(teamRepository.save(any())).thenReturn(team);

        teamService.createTeam(request);

        verify(events).publishEvent(any(TeamCreatedEvent.class));
    }

    @Test
    void updateTeam_shouldUpdateAndPublishEvent() {
        UpdateTeamRequest request = new UpdateTeamRequest(1L, "Updated Team", "new desc", TeamRole.ADMIN);
        TeamEntity team = new TeamEntity(1L, "Test Team", "desc", TeamRole.OPS);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any())).thenReturn(team);

        teamService.updateTeam(request);

        verify(events).publishEvent(any(TeamUpdatedEvent.class));
    }

    @Test
    void deleteTeam_shouldDeleteAndPublishEvent() {
        when(teamRepository.existsById(1L)).thenReturn(true);
        doNothing().when(teamRepository).deleteById(1L);

        teamService.deleteTeam(1L);

        verify(events).publishEvent(any(TeamDeletedEvent.class));
    }

    @Test
    void updateTeam_shouldThrowException_whenTeamNotFound() {
        UpdateTeamRequest request = new UpdateTeamRequest(1L, "Updated Team", "new desc", TeamRole.ADMIN);
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(request));
    }
}
