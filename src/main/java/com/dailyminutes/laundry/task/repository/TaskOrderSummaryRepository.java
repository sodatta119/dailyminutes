/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.laundry.task.domain.model.TaskOrderSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskOrderSummaryRepository extends ListCrudRepository<TaskOrderSummaryEntity, Long> {
    Optional<TaskOrderSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events

    Optional<TaskOrderSummaryEntity> findByOrderId(Long orderId); // Useful for updates from Order events

    List<TaskOrderSummaryEntity> findByCustomerId(Long customerId);

    List<TaskOrderSummaryEntity> findByStoreId(Long storeId);

    List<TaskOrderSummaryEntity> findByStatus(String status);
}
