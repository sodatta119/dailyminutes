package com.dailyminutes.laundry.agent.repository;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
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
 * The type Agent repository test.
 */
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.agent.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.agent.domain.model"})
class AgentRepositoryTest {

    @Autowired
    private AgentRepository agentRepository;

    /**
     * Test save and find agent.
     */
    @Test
    void testSaveAndFindAgent() {
        AgentEntity agent = new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "9876543210", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT); // Updated
        AgentEntity savedAgent = agentRepository.save(agent);

        assertThat(savedAgent).isNotNull();
        assertThat(savedAgent.getId()).isNotNull();

        Optional<AgentEntity> foundAgent = agentRepository.findById(savedAgent.getId());
        assertThat(foundAgent).isPresent();
        assertThat(foundAgent.get().getName()).isEqualTo("Agent Alpha");
        assertThat(foundAgent.get().getUniqueId()).isEqualTo("A001");
    }

    /**
     * Test find by phone number.
     */
    @Test
    void testFindByPhoneNumber() {
        AgentEntity agent = new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, 10l, "1112223333", "A002", LocalDate.now(), null, AgentDesignation.STORE_AGENT); // Updated
        agentRepository.save(agent);
        Optional<AgentEntity> foundAgent = agentRepository.findByPhoneNumber("1112223333");
        assertThat(foundAgent).isPresent();
        assertThat(foundAgent.get().getName()).isEqualTo("Agent Beta");
    }

    /**
     * Test find by unique id.
     */
    @Test
    void testFindByUniqueId() {
        AgentEntity agent = new AgentEntity(null, "Agent Gamma", AgentState.INACTIVE, 10l, "4445556666", "A003", LocalDate.now(), LocalDate.now().plusDays(10), AgentDesignation.PROCESS_MANAGER);
        agentRepository.save(agent);
        Optional<AgentEntity> foundAgent = agentRepository.findByUniqueId("A003");
        assertThat(foundAgent).isPresent();
        assertThat(foundAgent.get().getName()).isEqualTo("Agent Gamma");
    }

    /**
     * Test find by team id.
     */
    @Test
    void testFindByTeamId() {
        agentRepository.save(new AgentEntity(null, "Agent Delta", AgentState.ACTIVE, 10l, "7778889999", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        agentRepository.save(new AgentEntity(null, "Agent Epsilon", AgentState.ACTIVE, 10l, "0001112222", "A005", LocalDate.now(), null, AgentDesignation.STORE_AGENT));
        agentRepository.save(new AgentEntity(null, "Agent Zeta", AgentState.INACTIVE, 20l, "3334445555", "A006", LocalDate.now(), null, AgentDesignation.CUSTOMER_SUPPORT));

        List<AgentEntity> agents = agentRepository.findByTeamId(10l);
        assertThat(agents).hasSize(2);
        assertThat(agents.get(0).getTeamId()).isEqualTo(10l);
    }

    /**
     * Test find by state.
     */
    @Test
    void testFindByState() {
        agentRepository.save(new AgentEntity(null, "Agent Eta", AgentState.ACTIVE, 10l, "6667778888", "A007", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        agentRepository.save(new AgentEntity(null, "Agent Theta", AgentState.INACTIVE, 10l, "9990001111", "A008", LocalDate.now(), null, AgentDesignation.STORE_AGENT));

        List<AgentEntity> activeAgents = agentRepository.findByState(AgentState.ACTIVE);
        assertThat(activeAgents).hasSize(1);
        assertThat(activeAgents.get(0).getState()).isEqualTo(AgentState.ACTIVE);
    }

    /**
     * Test find by designation.
     */
    @Test
    void testFindByDesignation() {
        agentRepository.save(new AgentEntity(null, "Agent Iota", AgentState.ACTIVE, 10l, "1231231234", "A009", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        agentRepository.save(new AgentEntity(null, "Agent Kappa", AgentState.ACTIVE, 20l, "4564564567", "A010", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        agentRepository.save(new AgentEntity(null, "Agent Lambda", AgentState.ACTIVE, 30l, "7897897890", "A011", LocalDate.now(), null, AgentDesignation.STORE_AGENT));

        List<AgentEntity> fieldAgents = agentRepository.findByDesignation(AgentDesignation.FLEET_AGENT);
        assertThat(fieldAgents).hasSize(2);
        assertThat(fieldAgents.get(0).getDesignation()).isEqualTo(AgentDesignation.FLEET_AGENT);
    }

    /**
     * Test update agent.
     */
    @Test
    void testUpdateAgent() {
        AgentEntity agent = new AgentEntity(null, "Agent Update", AgentState.ACTIVE, 10l, "1002003000", "U001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT); // Updated
        AgentEntity savedAgent = agentRepository.save(agent);

        savedAgent.setState(AgentState.INACTIVE);
        savedAgent.setTerminationDate(LocalDate.now());
        AgentEntity updatedAgent = agentRepository.save(savedAgent);

        Optional<AgentEntity> foundUpdatedAgent = agentRepository.findById(updatedAgent.getId());
        assertThat(foundUpdatedAgent).isPresent();
        assertThat(foundUpdatedAgent.get().getState()).isEqualTo(AgentState.INACTIVE);
        assertThat(foundUpdatedAgent.get().getTerminationDate()).isEqualTo(LocalDate.now());
    }

    /**
     * Test delete agent.
     */
    @Test
    void testDeleteAgent() {
        AgentEntity agent = new AgentEntity(null, "Agent Delete", AgentState.ACTIVE, 10l, "9999999999", "D001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT); // Updated
        AgentEntity savedAgent = agentRepository.save(agent);

        agentRepository.deleteById(savedAgent.getId());

        Optional<AgentEntity> deletedAgent = agentRepository.findById(savedAgent.getId());
        assertThat(deletedAgent).isNotPresent();
    }
}