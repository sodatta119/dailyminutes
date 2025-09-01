/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.team.repository;

import com.dailyminutes.laundry.team.domain.model.TeamAgentSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Team agent summary repository.
 */
public interface TeamAgentSummaryRepository extends ListCrudRepository<TeamAgentSummaryEntity, Long> {
    /**
     * Find by team id list.
     *
     * @param teamId the team id
     * @return the list
     */
    List<TeamAgentSummaryEntity> findByTeamId(Long teamId);

    /**
     * Find by agent id optional.
     *
     * @param agentId the agent id
     * @return the optional
     */
    Optional<TeamAgentSummaryEntity> findByAgentId(Long agentId); // Useful for updates from Agent events

    /**
     * Find by agent designation list.
     *
     * @param agentDesignation the agent designation
     * @return the list
     */
    List<TeamAgentSummaryEntity> findByAgentDesignation(String agentDesignation);

    /**
     * Find by agent state list.
     *
     * @param agentState the agent state
     * @return the list
     */
    List<TeamAgentSummaryEntity> findByAgentState(String agentState);

    /**
     * Delete by agent id.
     *
     * @param aLong the a long
     */
    void deleteByAgentId(Long aLong);

    /**
     * Find by team id and agent id optional.
     *
     * @param teamId  the team id
     * @param agentId the agent id
     * @return the optional
     */
    Optional<TeamAgentSummaryEntity> findByTeamIdAndAgentId(Long teamId, Long agentId);

}
