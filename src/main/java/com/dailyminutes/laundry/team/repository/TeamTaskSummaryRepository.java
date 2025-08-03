/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.team.repository; // Updated package name

import com.dailyminutes.laundry.team.domain.model.TeamTaskSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface TeamTaskSummaryRepository extends ListCrudRepository<TeamTaskSummaryEntity, Long> {
    List<TeamTaskSummaryEntity> findByTeamId(Long teamId);

    Optional<TeamTaskSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events

    List<TeamTaskSummaryEntity> findByAgentId(Long agentId);

    List<TeamTaskSummaryEntity> findByTaskStatus(String taskStatus);

    List<TeamTaskSummaryEntity> findByTaskType(String taskType);

    List<TeamTaskSummaryEntity> findByOrderId(Long orderId);
}
