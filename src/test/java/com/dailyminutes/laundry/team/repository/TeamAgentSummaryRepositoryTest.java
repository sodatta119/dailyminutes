package com.dailyminutes.laundry.team.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.team.domain.model.TeamAgentSummaryEntity;
import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.domain.model.TeamRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Team agent summary repository test.
 *
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16 /07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.team.repository")
@ComponentScan(basePackages = "com.dailyminutes.laundry.team.domain.model")
class TeamAgentSummaryRepositoryTest {

    @Autowired
    private TeamAgentSummaryRepository teamAgentSummaryRepository;

    @Autowired
    private TeamRepository teamRepository;

    private String generateUniquePhoneNumber() {
        return "9" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    /**
     * Test save and find team agent summary.
     */
    @Test
    void testSaveAndFindTeamAgentSummary() {
        TeamEntity team = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS));
        String phoneNumber = generateUniquePhoneNumber();

        TeamAgentSummaryEntity summary = new TeamAgentSummaryEntity(null, team.getId(), 10l, "Agent Alpha", phoneNumber, "FLEET_AGENT", "ACTIVE");
        TeamAgentSummaryEntity savedSummary = teamAgentSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getTeamId()).isEqualTo(team.getId());
        assertThat(savedSummary.getAgentId()).isEqualTo(10l);
        assertThat(savedSummary.getAgentName()).isEqualTo("Agent Alpha");
        assertThat(savedSummary.getAgentPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedSummary.getAgentDesignation()).isEqualTo("FLEET_AGENT");
        assertThat(savedSummary.getAgentState()).isEqualTo("ACTIVE");

        Optional<TeamAgentSummaryEntity> foundSummary = teamAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAgentName()).isEqualTo("Agent Alpha");
    }

    /**
     * Test update team agent summary.
     */
    @Test
    void testUpdateTeamAgentSummary() {
        TeamEntity team = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS));
        String phoneNumber = generateUniquePhoneNumber();

        TeamAgentSummaryEntity summary = new TeamAgentSummaryEntity(null, team.getId(), 10l, "Agent Beta", phoneNumber, "DELIVERY_EXECUTIVE", "ACTIVE");
        TeamAgentSummaryEntity savedSummary = teamAgentSummaryRepository.save(summary);

        savedSummary.setAgentState("INACTIVE");
        savedSummary.setAgentName("Agent Beta (Inactive)");
        teamAgentSummaryRepository.save(savedSummary);

        Optional<TeamAgentSummaryEntity> updatedSummary = teamAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getAgentState()).isEqualTo("INACTIVE");
        assertThat(updatedSummary.get().getAgentName()).isEqualTo("Agent Beta (Inactive)");
    }

    /**
     * Test delete team agent summary.
     */
    @Test
    void testDeleteTeamAgentSummary() {
        TeamEntity team = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS));
        String phoneNumber = generateUniquePhoneNumber();

        TeamAgentSummaryEntity summary = new TeamAgentSummaryEntity(null, team.getId(), 10l, "Agent Gamma", phoneNumber, "PROCESS_MANAGER", "ACTIVE");
        TeamAgentSummaryEntity savedSummary = teamAgentSummaryRepository.save(summary);

        teamAgentSummaryRepository.deleteById(savedSummary.getId());
        Optional<TeamAgentSummaryEntity> deletedSummary = teamAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    /**
     * Test find by team id.
     */
    @Test
    void testFindByTeamId() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team1.getId(), 10l, "Agent D", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team1.getId(), 20l, "Agent E", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team2.getId(), 30l, "Agent F", generateUniquePhoneNumber(), "PROCESS_MANAGER", "ACTIVE"));

        List<TeamAgentSummaryEntity> summaries = teamAgentSummaryRepository.findByTeamId(team1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getTeamId().equals(team1.getId()))).isTrue();
    }

    /**
     * Test find by agent id.
     */
    @Test
    void testFindByAgentId() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team1.getId(), 10l, "Agent G", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team2.getId(), 20l, "Agent H", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));

        Optional<TeamAgentSummaryEntity> foundSummary = teamAgentSummaryRepository.findByAgentId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAgentName()).isEqualTo("Agent G");
    }

    /**
     * Test find by agent designation.
     */
    @Test
    void testFindByAgentDesignation() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS));
        TeamEntity team3 = teamRepository.save(new TeamEntity(null, "Operations Team C", "Handles daily operations", TeamRole.OPS));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team1.getId(), 10l, "Agent I", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team2.getId(), 20l, "Agent J", generateUniquePhoneNumber(), "FLEET_AGENT", "INACTIVE"));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team3.getId(), 30l, "Agent K", generateUniquePhoneNumber(), "PROCESS_MANAGER", "ACTIVE"));

        List<TeamAgentSummaryEntity> fleetAgents = teamAgentSummaryRepository.findByAgentDesignation("FLEET_AGENT");
        assertThat(fleetAgents).hasSize(2);
        assertThat(fleetAgents.stream().allMatch(s -> s.getAgentDesignation().equals("FLEET_AGENT"))).isTrue();
    }

    /**
     * Test find by agent state.
     */
    @Test
    void testFindByAgentState() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS));
        TeamEntity team3 = teamRepository.save(new TeamEntity(null, "Operations Team C", "Handles daily operations", TeamRole.OPS));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team1.getId(), 10l, "Agent L", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team2.getId(), 20l, "Agent M", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));
        teamAgentSummaryRepository.save(new TeamAgentSummaryEntity(null, team3.getId(), 30l, "Agent N", generateUniquePhoneNumber(), "PROCESS_MANAGER", "INACTIVE"));

        List<TeamAgentSummaryEntity> activeAgents = teamAgentSummaryRepository.findByAgentState("ACTIVE");
        assertThat(activeAgents).hasSize(2);
        assertThat(activeAgents.stream().allMatch(s -> s.getAgentState().equals("ACTIVE"))).isTrue();
    }
}
