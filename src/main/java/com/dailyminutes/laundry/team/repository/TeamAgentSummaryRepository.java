/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.team.repository;

import com.dailyminutes.laundry.team.domain.model.TeamAgentSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface TeamAgentSummaryRepository extends ListCrudRepository<TeamAgentSummaryEntity, Long> {
    List<TeamAgentSummaryEntity> findByTeamId(Long teamId);
    Optional<TeamAgentSummaryEntity> findByAgentId(Long agentId); // Useful for updates from Agent events
    List<TeamAgentSummaryEntity> findByAgentDesignation(String agentDesignation);
    List<TeamAgentSummaryEntity> findByAgentState(String agentState);
}
