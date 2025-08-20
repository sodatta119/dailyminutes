/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.laundry.task.domain.model.TaskTeamSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Task team summary repository.
 */
public interface TaskTeamSummaryRepository extends ListCrudRepository<TaskTeamSummaryEntity, Long> {
    /**
     * Find by task id optional.
     *
     * @param taskId the task id
     * @return the optional
     */
    Optional<TaskTeamSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events

    /**
     * Find by team id list.
     *
     * @param teamId the team id
     * @return the list
     */
    List<TaskTeamSummaryEntity> findByTeamId(Long teamId); // Useful for updates from Team events

    /**
     * Find by team role list.
     *
     * @param teamRole the team role
     * @return the list
     */
    List<TaskTeamSummaryEntity> findByTeamRole(String teamRole);
}
