package com.chitchatfm.dailyminutes.laundry.team.repository;

import com.chitchatfm.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.chitchatfm.dailyminutes.laundry.team.domain.model.TeamRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Team repository test.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Test save and find team.
     */
    @Test
    void testSaveAndFindTeam() {
        TeamEntity team = new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS);
        TeamEntity savedTeam = teamRepository.save(team);

        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getId()).isNotNull();

        Optional<TeamEntity> foundTeam = teamRepository.findById(savedTeam.getId());
        assertThat(foundTeam).isPresent();
        assertThat(foundTeam.get().getName()).isEqualTo("Operations Team A");
    }

    /**
     * Test update team.
     */
    @Test
    void testUpdateTeam() {
        TeamEntity team = new TeamEntity(null, "Support Team Alpha", "Customer support", TeamRole.SUPPORT);
        TeamEntity savedTeam = teamRepository.save(team);

        TeamEntity foundTeam = teamRepository.findById(savedTeam.getId()).orElseThrow();
        foundTeam.setDescription("Primary customer support team");
        teamRepository.save(foundTeam);

        Optional<TeamEntity> updatedTeam = teamRepository.findById(savedTeam.getId());
        assertThat(updatedTeam).isPresent();
        assertThat(updatedTeam.get().getDescription()).isEqualTo("Primary customer support team");
    }

    /**
     * Test delete team.
     */
    @Test
    void testDeleteTeam() {
        TeamEntity team = new TeamEntity(null, "Fleet Team X", "Vehicle management", TeamRole.FLEET);
        TeamEntity savedTeam = teamRepository.save(team);

        teamRepository.deleteById(savedTeam.getId());
        Optional<TeamEntity> deletedTeam = teamRepository.findById(savedTeam.getId());
        assertThat(deletedTeam).isNotPresent();
    }
}
