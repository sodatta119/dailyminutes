package com.chitchatfm.dailyminutes.laundry.team.repository;

import com.chitchatfm.dailyminutes.laundry.team.domain.model.TeamEntity;
import org.springframework.data.repository.ListCrudRepository;

/**
 * The interface Team repository.
 */
public interface TeamRepository extends ListCrudRepository<TeamEntity, Long> {
}

