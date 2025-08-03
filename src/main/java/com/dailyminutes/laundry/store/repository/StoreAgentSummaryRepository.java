/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.repository; // Updated package name

import com.dailyminutes.laundry.store.domain.model.StoreAgentSummaryEntity; // Updated import
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface StoreAgentSummaryRepository extends ListCrudRepository<StoreAgentSummaryEntity, Long> {
    List<StoreAgentSummaryEntity> findByStoreId(Long storeId);

    Optional<StoreAgentSummaryEntity> findByAgentId(Long agentId); // Useful for updates from Agent events

    List<StoreAgentSummaryEntity> findByAgentDesignation(String agentDesignation);

    List<StoreAgentSummaryEntity> findByAgentStatus(String agentStatus);
}
