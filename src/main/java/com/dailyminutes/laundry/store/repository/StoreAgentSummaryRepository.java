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

/**
 * The interface Store agent summary repository.
 */
public interface StoreAgentSummaryRepository extends ListCrudRepository<StoreAgentSummaryEntity, Long> {
    /**
     * Find by store id list.
     *
     * @param storeId the store id
     * @return the list
     */
    List<StoreAgentSummaryEntity> findByStoreId(Long storeId);

    /**
     * Find by agent id optional.
     *
     * @param agentId the agent id
     * @return the optional
     */
    Optional<StoreAgentSummaryEntity> findByAgentId(Long agentId); // Useful for updates from Agent events

    /**
     * Find by agent designation list.
     *
     * @param agentDesignation the agent designation
     * @return the list
     */
    List<StoreAgentSummaryEntity> findByAgentDesignation(String agentDesignation);

    /**
     * Find by agent status list.
     *
     * @param agentStatus the agent status
     * @return the list
     */
    List<StoreAgentSummaryEntity> findByAgentStatus(String agentStatus);
}
