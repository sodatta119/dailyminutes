/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */

package com.dailyminutes.laundry.agent.repository;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Agent repository.
 */
public interface AgentRepository extends ListCrudRepository<AgentEntity, Long> {
    /**
     * Find by phone number optional.
     *
     * @param phoneNumber the phone number
     * @return the optional
     */
    Optional<AgentEntity> findByPhoneNumber(String phoneNumber);

    /**
     * Find by unique id optional.
     *
     * @param uniqueId the unique id
     * @return the optional
     */
    Optional<AgentEntity> findByUniqueId(String uniqueId);

    /**
     * Find by team id list.
     *
     * @param teamId the team id
     * @return the list
     */
    List<AgentEntity> findByTeamId(Long teamId);

    /**
     * Find by external id optional.
     *
     * @param teamId the team id
     * @return the optional
     */
    Optional<AgentEntity> findByExternalId(String teamId);

    /**
     * Find by state list.
     *
     * @param state the state
     * @return the list
     */
    List<AgentEntity> findByState(AgentState state);

    /**
     * Find by designation list.
     *
     * @param designation the designation
     * @return the list
     */
    List<AgentEntity> findByDesignation(AgentDesignation designation);
}

