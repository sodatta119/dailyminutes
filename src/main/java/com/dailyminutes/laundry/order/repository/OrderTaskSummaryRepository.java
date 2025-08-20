/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.order.repository; // Updated package name

import com.dailyminutes.laundry.order.domain.model.OrderTaskSummaryEntity; // Updated import
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Order task summary repository.
 */
public interface OrderTaskSummaryRepository extends ListCrudRepository<OrderTaskSummaryEntity, Long> {
    /**
     * Find by order id optional.
     *
     * @param orderId the order id
     * @return the optional
     */
    Optional<OrderTaskSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    /**
     * Find by task id optional.
     *
     * @param taskId the task id
     * @return the optional
     */
    Optional<OrderTaskSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events

    /**
     * Find by agent id list.
     *
     * @param agentId the agent id
     * @return the list
     */
    List<OrderTaskSummaryEntity> findByAgentId(Long agentId);

    /**
     * Find by task status list.
     *
     * @param taskStatus the task status
     * @return the list
     */
    List<OrderTaskSummaryEntity> findByTaskStatus(String taskStatus);
}
