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

public interface StoreTaskSummaryRepository extends ListCrudRepository<StoreTaskSummaryEntity, Long> {
    List<StoreTaskSummaryEntity> findByStoreId(Long storeId);
    Optional<StoreTaskSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events
    List<StoreTaskSummaryEntity> findByAgentId(Long agentId);
    List<StoreTaskSummaryEntity> findByTaskStatus(String taskStatus);
    List<StoreTaskSummaryEntity> findByTaskType(String taskType);
    List<StoreTaskSummaryEntity> findByOrderId(Long orderId);
}
