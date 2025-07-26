/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.api;

import com.dailyminutes.laundry.team.dto.CreateTeamRequest;
import com.dailyminutes.laundry.team.dto.TeamAgentSummaryResponse;
import com.dailyminutes.laundry.team.dto.TeamResponse;
import com.dailyminutes.laundry.team.dto.TeamTaskSummaryResponse;
import com.dailyminutes.laundry.team.dto.UpdateTeamRequest;
import com.dailyminutes.laundry.team.service.TeamQueryService;
import com.dailyminutes.laundry.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamQueryService teamQueryService;

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        return new ResponseEntity<>(teamService.createTeam(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long id, @Valid @RequestBody UpdateTeamRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(teamService.updateTeam(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long id) {
        return teamQueryService.findTeamById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(teamQueryService.findAllTeams());
    }

    @GetMapping("/{id}/agent-summary")
    public ResponseEntity<List<TeamAgentSummaryResponse>> getAgentSummary(@PathVariable Long id) {
        return ResponseEntity.ok(teamQueryService.findAgentSummariesByTeamId(id));
    }

    @GetMapping("/{id}/task-summary")
    public ResponseEntity<List<TeamTaskSummaryResponse>> getTaskSummary(@PathVariable Long id) {
        return ResponseEntity.ok(teamQueryService.findTaskSummariesByTeamId(id));
    }
}
