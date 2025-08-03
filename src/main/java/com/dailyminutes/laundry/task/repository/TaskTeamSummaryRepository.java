/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.laundry.task.domain.model.TaskTeamSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskTeamSummaryRepository extends ListCrudRepository<TaskTeamSummaryEntity, Long> {
    Optional<TaskTeamSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events

    List<TaskTeamSummaryEntity> findByTeamId(Long teamId); // Useful for updates from Team events

    List<TaskTeamSummaryEntity> findByTeamRole(String teamRole);
}
