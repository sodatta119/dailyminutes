/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.task.repository;

import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskType;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Task repository.
 */
public interface TaskRepository extends ListCrudRepository<TaskEntity, Long> {
    /**
     * Find by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    List<TaskEntity> findByOrderId(Long orderId);

    /**
     * Find by agent id list.
     *
     * @param agentId the agent id
     * @return the list
     */
    List<TaskEntity> findByAgentId(Long agentId);

    /**
     * Find by team id list.
     *
     * @param teamId the team id
     * @return the list
     */
    List<TaskEntity> findByTeamId(Long teamId);

    /**
     * Find by status list.
     *
     * @param status the status
     * @return the list
     */
    List<TaskEntity> findByStatus(TaskStatus status);

    /**
     * Find by type list.
     *
     * @param type the type
     * @return the list
     */
    List<TaskEntity> findByType(TaskType type);

    /**
     * Find by name optional.
     *
     * @param specificTaskName the specific task name
     * @return the optional
     */
    Optional<TaskEntity> findByName(String specificTaskName);
}

