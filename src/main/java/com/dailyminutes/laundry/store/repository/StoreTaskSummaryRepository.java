/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.repository; // Updated package name

import com.dailyminutes.laundry.store.domain.model.StoreTaskSummaryEntity; // Updated import
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Store task summary repository.
 */
public interface StoreTaskSummaryRepository extends ListCrudRepository<StoreTaskSummaryEntity, Long> {
    /**
     * Find by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    List<StoreTaskSummaryEntity> findByStoreId(Long storeId);

    /**
     * Find by task id optional.
     *
     * @param taskId the task id
     * @return the optional
     */
    Optional<StoreTaskSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events

    /**
     * Find by agent id list.
     *
     * @param agentId the agent id
     * @return the list
     */
    List<StoreTaskSummaryEntity> findByAgentId(Long agentId);

    /**
     * Find by task status list.
     *
     * @param taskStatus the task status
     * @return the list
     */
    List<StoreTaskSummaryEntity> findByTaskStatus(String taskStatus);

    /**
     * Find by task type list.
     *
     * @param taskType the task type
     * @return the list
     */
    List<StoreTaskSummaryEntity> findByTaskType(String taskType);

    /**
     * Find by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    List<StoreTaskSummaryEntity> findByOrderId(Long orderId);
}
