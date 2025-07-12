/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
package com.chitchatfm.dailyminutes.laundry.agent.repository;
import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentTeamSummaryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AgentTeamSummaryRepository extends CrudRepository<AgentTeamSummaryEntity, Long> {
    Optional<AgentTeamSummaryEntity> findByTeamName(String teamName);

    List<AgentTeamSummaryEntity> findByAgentId(long l);
}

