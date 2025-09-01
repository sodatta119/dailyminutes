/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.service;

import com.dailyminutes.laundry.team.domain.event.TeamCreatedEvent;
import com.dailyminutes.laundry.team.domain.event.TeamDeletedEvent;
import com.dailyminutes.laundry.team.domain.event.TeamSyncEvent;
import com.dailyminutes.laundry.team.domain.event.TeamUpdatedEvent;
import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.domain.model.TeamRole;
import com.dailyminutes.laundry.team.dto.CreateTeamRequest;
import com.dailyminutes.laundry.team.dto.TeamResponse;
import com.dailyminutes.laundry.team.dto.UpdateTeamRequest;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * The type Team service.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final ApplicationEventPublisher events;

    /**
     * Create team team response.
     *
     * @param request the request
     * @return the team response
     */
    public TeamResponse createTeam(CreateTeamRequest request) {
        TeamEntity team = new TeamEntity(null, request.name(), request.description(), request.role(), null, LocalDateTime.now(), false);
        TeamEntity savedTeam = teamRepository.save(team);
        events.publishEvent(new TeamCreatedEvent(savedTeam.getId(), savedTeam.getName()));
        return toTeamResponse(savedTeam);
    }

    /**
     * Upsert from sync.
     *
     * @param payload the payload
     */
    public void upsertFromSync(TeamSyncEvent.TeamSyncPayload payload) {
        TeamEntity team = teamRepository.findByExternalId(payload.externalId())
                .orElseGet(() -> new TeamEntity(null, payload.name(), payload.description(), TeamRole.ADMIN, payload.externalId(), LocalDateTime.now(), !payload.active()));
        TeamEntity savedTeam = teamRepository.save(team);
        events.publishEvent(new TeamCreatedEvent(savedTeam.getId(), savedTeam.getName()));
    }

    /**
     * Update team team response.
     *
     * @param request the request
     * @return the team response
     */
    public TeamResponse updateTeam(UpdateTeamRequest request) {
        TeamEntity existingTeam = teamRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Team with ID " + request.id() + " not found."));

        existingTeam.setName(request.name());
        existingTeam.setDescription(request.description());
        existingTeam.setRole(request.role());

        TeamEntity updatedTeam = teamRepository.save(existingTeam);
        events.publishEvent(new TeamUpdatedEvent(updatedTeam.getId(), updatedTeam.getName()));
        return toTeamResponse(updatedTeam);
    }

    /**
     * Delete team.
     *
     * @param id the id
     */
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new IllegalArgumentException("Team with ID " + id + " not found.");
        }
        teamRepository.deleteById(id);
        events.publishEvent(new TeamDeletedEvent(id));
    }

    private TeamResponse toTeamResponse(TeamEntity entity) {
        return new TeamResponse(entity.getId(), entity.getName(), entity.getDescription(), entity.getRole());
    }
}
