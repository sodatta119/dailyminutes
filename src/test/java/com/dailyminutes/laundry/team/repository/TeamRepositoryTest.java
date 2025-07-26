package com.dailyminutes.laundry.team.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.domain.model.TeamRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Team repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class) // Exclude main app class
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.team.repository") // Specify repository package
@ComponentScan(basePackages = "com.dailyminutes.laundry.team.domain.model") // Specify domain model package
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
        assertThat(foundTeam.get().getRole()).isEqualTo(TeamRole.OPS);
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
        foundTeam.setRole(TeamRole.ADMIN); // Change role for update test
        teamRepository.save(foundTeam);

        Optional<TeamEntity> updatedTeam = teamRepository.findById(savedTeam.getId());
        assertThat(updatedTeam).isPresent();
        assertThat(updatedTeam.get().getDescription()).isEqualTo("Primary customer support team");
        assertThat(updatedTeam.get().getRole()).isEqualTo(TeamRole.ADMIN);
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

    /**
     * Test find by name.
     */
    @Test
    void testFindByName() {
        teamRepository.save(new TeamEntity(null, "Unique Team Name", "A unique team", TeamRole.OPS));
        Optional<TeamEntity> foundTeam = teamRepository.findByName("Unique Team Name");
        assertThat(foundTeam).isPresent();
        assertThat(foundTeam.get().getRole()).isEqualTo(TeamRole.OPS);
    }
}
