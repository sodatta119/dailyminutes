package com.dailyminutes.laundry.team.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.dailyminutes.laundry.team.domain.model.TeamRole;
import com.dailyminutes.laundry.team.domain.model.TeamTaskSummaryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Team task summary repository test.
 *
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16 /07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.team.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.team.domain.model") // Updated package name
class TeamTaskSummaryRepositoryTest {

    @Autowired
    private TeamTaskSummaryRepository teamTaskSummaryRepository;

    @Autowired
    private TeamRepository teamRepository;


    /**
     * Test save and find team task summary.
     */
    @Test
    void testSaveAndFindTeamTaskSummary() {
        TeamEntity team = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));

        TeamTaskSummaryEntity summary = new TeamTaskSummaryEntity(null, team.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Team Agent Alpha", 10l);
        TeamTaskSummaryEntity savedSummary = teamTaskSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getTeamId()).isEqualTo(team.getId());
        assertThat(savedSummary.getTaskId()).isEqualTo(10l);
        assertThat(savedSummary.getTaskType()).isEqualTo("PICKUP");
        assertThat(savedSummary.getTaskStatus()).isEqualTo("NEW");
        assertThat(savedSummary.getAgentId()).isEqualTo(10l);
        assertThat(savedSummary.getAgentName()).isEqualTo("Team Agent Alpha");
        assertThat(savedSummary.getOrderId()).isEqualTo(10l);

        Optional<TeamTaskSummaryEntity> foundSummary = teamTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskType()).isEqualTo("PICKUP");
    }

    /**
     * Test update team task summary.
     */
    @Test
    void testUpdateTeamTaskSummary() {
        TeamEntity team = teamRepository.save(new TeamEntity(null,  "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));

        TeamTaskSummaryEntity summary = new TeamTaskSummaryEntity(null, team.getId(), 10l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 10l, "Team Agent Beta", 10l);
        TeamTaskSummaryEntity savedSummary = teamTaskSummaryRepository.save(summary);

        savedSummary.setTaskStatus("COMPLETED");
        savedSummary.setAgentName("Team Agent Beta (Completed)");
        teamTaskSummaryRepository.save(savedSummary);

        Optional<TeamTaskSummaryEntity> updatedSummary = teamTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getTaskStatus()).isEqualTo("COMPLETED");
        assertThat(updatedSummary.get().getAgentName()).isEqualTo("Team Agent Beta (Completed)");
    }

    /**
     * Test delete team task summary.
     */
    @Test
    void testDeleteTeamTaskSummary() {
        TeamEntity team = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));

        TeamTaskSummaryEntity summary = new TeamTaskSummaryEntity(null, team.getId(), 10l, "PROCESS", "STARTED", LocalDateTime.now(), 10l, "Team Agent Gamma", 10l);
        TeamTaskSummaryEntity savedSummary = teamTaskSummaryRepository.save(summary);

        teamTaskSummaryRepository.deleteById(savedSummary.getId());
        Optional<TeamTaskSummaryEntity> deletedSummary = teamTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    /**
     * Test find by team id.
     */
    @Test
    void testFindByTeamId() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS, "999L", LocalDateTime.now(), false));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent D", 10l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team1.getId(), 20l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 20l, "Agent E", 20l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team2.getId(), 30l, "PROCESS", "STARTED", LocalDateTime.now(), 30l, "Agent F", 30l));

        List<TeamTaskSummaryEntity> summaries = teamTaskSummaryRepository.findByTeamId(team1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getTeamId().equals(team1.getId()))).isTrue();
    }

    /**
     * Test find by task id.
     */
    @Test
    void testFindByTaskId() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS, "999L", LocalDateTime.now(), false));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team1.getId(), 10l, "PROCESS", "COMPLETED", LocalDateTime.now(), 10l, "Agent G", 10l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team2.getId(), 20l, "PICKUP", "NEW", LocalDateTime.now(), 20l, "Agent H", 20l));

        Optional<TeamTaskSummaryEntity> foundSummary = teamTaskSummaryRepository.findByTaskId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskStatus()).isEqualTo("COMPLETED");
    }

    /**
     * Test find by agent id.
     */
    @Test
    void testFindByAgentId() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS, "999L", LocalDateTime.now(), false));
        TeamEntity team3 = teamRepository.save(new TeamEntity(null, "Operations Team C", "Handles daily operations", TeamRole.OPS, "9999L", LocalDateTime.now(), false));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team1.getId(), 10l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 10l, "Agent I", 10l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team2.getId(), 20l, "PICKUP", "STARTED", LocalDateTime.now(), 10l, "Agent J", 20l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team3.getId(), 30l, "PROCESS", "NEW", LocalDateTime.now(), 30l, "Agent K", 30l));

        List<TeamTaskSummaryEntity> summariesForAgent = teamTaskSummaryRepository.findByAgentId(10l);
        assertThat(summariesForAgent).hasSize(2);
        assertThat(summariesForAgent.stream().allMatch(s -> s.getAgentId().equals(10l))).isTrue();
    }

    /**
     * Test find by task status.
     */
    @Test
    void testFindByTaskStatus() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS, "999L", LocalDateTime.now(), false));
        TeamEntity team3 = teamRepository.save(new TeamEntity(null, "Operations Team C", "Handles daily operations", TeamRole.OPS, "9999L", LocalDateTime.now(), false));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent L", 10l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team2.getId(), 20l, "DELIVERY", "NEW", LocalDateTime.now(), 20l, "Agent M", 20l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team3.getId(), 30l, "PROCESS", "ASSIGNED", LocalDateTime.now(), 30l, "Agent N", 30l));

        List<TeamTaskSummaryEntity> newTasks = teamTaskSummaryRepository.findByTaskStatus("NEW");
        assertThat(newTasks).hasSize(2);
        assertThat(newTasks.stream().allMatch(s -> s.getTaskStatus().equals("NEW"))).isTrue();
    }

    /**
     * Test find by task type.
     */
    @Test
    void testFindByTaskType() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS, "999L", LocalDateTime.now(), false));
        TeamEntity team3 = teamRepository.save(new TeamEntity(null, "Operations Team C", "Handles daily operations", TeamRole.OPS, "9999L", LocalDateTime.now(), false));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent O", 10l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team2.getId(), 20l, "PICKUP", "ASSIGNED", LocalDateTime.now(), 20l, "Agent P", 20l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team3.getId(), 30l, "DELIVERY", "STARTED", LocalDateTime.now(), 30l, "Agent Q", 30l));

        List<TeamTaskSummaryEntity> pickupTasks = teamTaskSummaryRepository.findByTaskType("PICKUP");
        assertThat(pickupTasks).hasSize(2);
        assertThat(pickupTasks.stream().allMatch(s -> s.getTaskType().equals("PICKUP"))).isTrue();
    }

    /**
     * Test find by order id.
     */
    @Test
    void testFindByOrderId() {
        TeamEntity team1 = teamRepository.save(new TeamEntity(null, "Operations Team A", "Handles daily operations", TeamRole.OPS, "99L", LocalDateTime.now(), false));
        TeamEntity team2 = teamRepository.save(new TeamEntity(null, "Operations Team B", "Handles daily operations", TeamRole.OPS, "999L", LocalDateTime.now(), false));
        TeamEntity team3 = teamRepository.save(new TeamEntity(null, "Operations Team C", "Handles daily operations", TeamRole.OPS, "9999L", LocalDateTime.now(), false));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent R", 10l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team2.getId(), 20l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 20l, "Agent S", 10l));
        teamTaskSummaryRepository.save(new TeamTaskSummaryEntity(null, team3.getId(), 30l, "PROCESS", "STARTED", LocalDateTime.now(), 30l, "Agent T", 20l));

        List<TeamTaskSummaryEntity> tasksForOrder = teamTaskSummaryRepository.findByOrderId(10l);
        assertThat(tasksForOrder).hasSize(2);
        assertThat(tasksForOrder.stream().allMatch(s -> s.getOrderId().equals(10l))).isTrue();
    }
}
