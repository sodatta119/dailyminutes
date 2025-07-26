package com.dailyminutes.laundry.team.service;

import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.domain.model.TeamRole;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamQueryServiceTest {

    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private TeamQueryService teamQueryService;

    @Test
    void findTeamById_shouldReturnTeam() {
        TeamEntity team = new TeamEntity(1L, "Test Team", "desc", TeamRole.OPS);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        assertThat(teamQueryService.findTeamById(1L)).isPresent();
    }
}
