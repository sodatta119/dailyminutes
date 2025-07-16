package com.dailyminutes.laundry.agent.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.domain.model.AgentTeamSummaryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Agent team summary repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.agent.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.agent.domain.model"})
class AgentTeamSummaryRepositoryTest {

    @Autowired
    private AgentTeamSummaryRepository agentTeamSummaryRepository;

    @Autowired
    private AgentRepository agentRepository;



    /**
     * Test save and find agent team summary.
     */
    @Test
    void testSaveAndFindAgentTeamSummary() {
        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "8787879879", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentTeamSummaryEntity summary = new AgentTeamSummaryEntity(null, agent1.getId(), 10l, "Fleet Alpha", "Team managing fleet agents for Agent 1");
        AgentTeamSummaryEntity savedSummary = agentTeamSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull(); // Assert that ID is now generated
        assertThat(savedSummary.getTeamId()).isEqualTo(10l);
        assertThat(savedSummary.getAgentId()).isEqualTo(agent1.getId());

        Optional<AgentTeamSummaryEntity> foundSummary = agentTeamSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTeamName()).isEqualTo("Fleet Alpha");
        assertThat(foundSummary.get().getAgentId()).isEqualTo(agent1.getId());
    }

    /**
     * Test update agent team summary.
     */
    @Test
    void testUpdateAgentTeamSummary() {
        AgentEntity agent = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "8787879879", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentTeamSummaryEntity summary = new AgentTeamSummaryEntity(null, agent.getId(), 10l, "Ops Beta", "Team handling operations for Agent 2");
        AgentTeamSummaryEntity savedSummary = agentTeamSummaryRepository.save(summary);

        AgentTeamSummaryEntity foundSummary = agentTeamSummaryRepository.findById(savedSummary.getId()).orElseThrow();
        foundSummary.setTeamDescription("Updated description for Ops Beta and Agent 2");
        foundSummary.setTeamName("Ops Beta - Modified");
        agentTeamSummaryRepository.save(foundSummary);

        Optional<AgentTeamSummaryEntity> updatedSummary = agentTeamSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getTeamDescription()).isEqualTo("Updated description for Ops Beta and Agent 2");
        assertThat(updatedSummary.get().getTeamName()).isEqualTo("Ops Beta - Modified");
    }

    /**
     * Test delete agent team summary.
     */
    @Test
    void testDeleteAgentTeamSummary() {
        AgentEntity agent = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "8787879879", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentTeamSummaryEntity summary = new AgentTeamSummaryEntity(null, agent.getId(), 10l, "Support Gamma", "Customer support team for Agent 3");
        AgentTeamSummaryEntity savedSummary = agentTeamSummaryRepository.save(summary);

        agentTeamSummaryRepository.deleteById(savedSummary.getId());
        Optional<AgentTeamSummaryEntity> deletedSummary = agentTeamSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    /**
     * Test find by team name.
     */
    @Test
    void testFindByTeamName() {
        AgentEntity agent = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "8787879879", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        agentTeamSummaryRepository.save(new AgentTeamSummaryEntity(null, agent.getId(), 10l, "Unique Team Name", "A very unique team for Agent 4"));
        Optional<AgentTeamSummaryEntity> foundSummary = agentTeamSummaryRepository.findByTeamName("Unique Team Name");
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTeamId()).isEqualTo(10l);
        assertThat(foundSummary.get().getAgentId()).isEqualTo(agent.getId());
    }

    /**
     * Test find by agent id.
     */
    @Test
    void testFindByAgentId() {
        // Agent 5 belongs to Team 105
        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "876876868", "A003", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, 10l, "987987987", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent3 = agentRepository.save(new AgentEntity(null, "Agent Sigma", AgentState.ACTIVE, 10l, "5435345345", "A005", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated


        agentTeamSummaryRepository.save(new AgentTeamSummaryEntity(null, agent1.getId(), 10l, "Fleet Delta", "Team for Agent 5"));
        // Agent 6 also belongs to Team 105 (if this is a many-to-many or historical view)
        agentTeamSummaryRepository.save(new AgentTeamSummaryEntity(null, agent2.getId(), 20l, "Fleet Delta", "Team for Agent 6"));
        // Agent 7 belongs to Team 106
        agentTeamSummaryRepository.save(new AgentTeamSummaryEntity(null, agent3.getId(), 30l, "Ops Epsilon", "Team for Agent 7"));

        List<AgentTeamSummaryEntity> summariesForAgent5 = agentTeamSummaryRepository.findByAgentId(agent1.getId());
        assertThat(summariesForAgent5).hasSize(1);
        assertThat(summariesForAgent5.get(0).getTeamId()).isEqualTo(10l);

        List<AgentTeamSummaryEntity> summariesForAgent6 = agentTeamSummaryRepository.findByAgentId(agent3.getId());
        assertThat(summariesForAgent6).hasSize(1);
        assertThat(summariesForAgent6.get(0).getTeamId()).isEqualTo(30l);
    }
}
