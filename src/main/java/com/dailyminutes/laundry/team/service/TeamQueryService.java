/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.team.service;

import com.dailyminutes.laundry.team.dto.TeamAgentSummaryResponse;
import com.dailyminutes.laundry.team.dto.TeamResponse;
import com.dailyminutes.laundry.team.dto.TeamTaskSummaryResponse;
import com.dailyminutes.laundry.team.repository.TeamAgentSummaryRepository;
import com.dailyminutes.laundry.team.repository.TeamRepository;
import com.dailyminutes.laundry.team.repository.TeamTaskSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Team query service.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {

    private final TeamRepository teamRepository;
    private final TeamAgentSummaryRepository agentSummaryRepository;
    private final TeamTaskSummaryRepository taskSummaryRepository;

    /**
     * Find team by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<TeamResponse> findTeamById(Long id) {
        return teamRepository.findById(id)
                .map(t -> new TeamResponse(t.getId(), t.getName(), t.getDescription(), t.getRole()));
    }

    /**
     * Find all teams list.
     *
     * @return the list
     */
    public List<TeamResponse> findAllTeams() {
        return StreamSupport.stream(teamRepository.findAll().spliterator(), false)
                .map(t -> new TeamResponse(t.getId(), t.getName(), t.getDescription(), t.getRole()))
                .collect(Collectors.toList());
    }

    /**
     * Find agent summaries by team id list.
     *
     * @param teamId the team id
     * @return the list
     */
    public List<TeamAgentSummaryResponse> findAgentSummariesByTeamId(Long teamId) {
        return agentSummaryRepository.findByTeamId(teamId).stream()
                .map(s -> new TeamAgentSummaryResponse(s.getId(), s.getTeamId(), s.getAgentId(), s.getAgentName(), s.getAgentPhoneNumber(), s.getAgentDesignation(), s.getAgentState()))
                .collect(Collectors.toList());
    }

    /**
     * Find task summaries by team id list.
     *
     * @param teamId the team id
     * @return the list
     */
    public List<TeamTaskSummaryResponse> findTaskSummariesByTeamId(Long teamId) {
        return taskSummaryRepository.findByTeamId(teamId).stream()
                .map(s -> new TeamTaskSummaryResponse(s.getId(), s.getTeamId(), s.getTaskId(), s.getTaskType(), s.getTaskStatus(), s.getTaskStartTime(), s.getAgentId(), s.getAgentName(), s.getOrderId()))
                .collect(Collectors.toList());
    }
}
