/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.agent.repository;

import com.dailyminutes.laundry.agent.domain.model.AgentTeamSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Agent team summary repository.
 */
public interface AgentTeamSummaryRepository extends ListCrudRepository<AgentTeamSummaryEntity, Long> {
    /**
     * Find by team name optional.
     *
     * @param teamName the team name
     * @return the optional
     */
    Optional<AgentTeamSummaryEntity> findByTeamName(String teamName);

    /**
     * Find by agent id list.
     *
     * @param l the l
     * @return the list
     */
    List<AgentTeamSummaryEntity> findByAgentId(long l);
}

