/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
package com.chitchatfm.dailyminutes.laundry.agent.repository;

import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentTaskSummaryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AgentTaskSummaryRepository extends CrudRepository<AgentTaskSummaryEntity, Long> {
    List<AgentTaskSummaryEntity> findByAgentId(Long agentId); // Added a finder method for agentId
    List<AgentTaskSummaryEntity> findByTaskStatus(String taskStatus); // Example finder
}

