/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.laundry.task.domain.model.TaskAgentSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskAgentSummaryRepository extends ListCrudRepository<TaskAgentSummaryEntity, Long> {
    Optional<TaskAgentSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events
    Optional<TaskAgentSummaryEntity> findByAgentId(Long agentId); // Useful for updates from Agent events
    List<TaskAgentSummaryEntity> findByAgentDesignation(String agentDesignation);
    List<TaskAgentSummaryEntity> findByAgentState(String agentState);
}
