/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.agent.repository;

import com.dailyminutes.laundry.agent.domain.model.AgentTaskSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Agent task summary repository.
 */
public interface AgentTaskSummaryRepository extends ListCrudRepository<AgentTaskSummaryEntity, Long> {
    /**
     * Find by agent id list.
     *
     * @param agentId the agent id
     * @return the list
     */
    List<AgentTaskSummaryEntity> findByAgentId(Long agentId); // Added a finder method for agentId

    /**
     * Find by task status list.
     *
     * @param taskStatus the task status
     * @return the list
     */
    List<AgentTaskSummaryEntity> findByTaskStatus(String taskStatus); // Example finder

    /**
     * Find by task id optional.
     *
     * @param taskId the task id
     * @return the optional
     */
// ADD this new method
    Optional<AgentTaskSummaryEntity> findByTaskId(Long taskId);

    /**
     * Delete by task id.
     *
     * @param taskId the task id
     */
// ADD this new method
    void deleteByTaskId(Long taskId);
}

