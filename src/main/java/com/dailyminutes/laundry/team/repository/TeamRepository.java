/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.team.repository;

import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * The interface Team repository.
 */
public interface TeamRepository extends ListCrudRepository<TeamEntity, Long> {
    /**
     * Find by name optional.
     *
     * @param uniqueTeamName the unique team name
     * @return the optional
     */
    Optional<TeamEntity> findByName(String uniqueTeamName);


    Optional<TeamEntity> findByExternalId(String externalId);


}

