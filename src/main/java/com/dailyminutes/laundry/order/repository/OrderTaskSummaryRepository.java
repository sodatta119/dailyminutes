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

public interface OrderTaskSummaryRepository extends ListCrudRepository<OrderTaskSummaryEntity, Long> {
    Optional<OrderTaskSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    Optional<OrderTaskSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events

    List<OrderTaskSummaryEntity> findByAgentId(Long agentId);

    List<OrderTaskSummaryEntity> findByTaskStatus(String taskStatus);
}
