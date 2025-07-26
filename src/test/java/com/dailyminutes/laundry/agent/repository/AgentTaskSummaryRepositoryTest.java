/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.agent.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.domain.model.AgentTaskSummaryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Agent task summary repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.agent.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.agent.domain.model"})
class AgentTaskSummaryRepositoryTest {

    @Autowired
    private AgentTaskSummaryRepository agentTaskSummaryRepository;

    @Autowired
    private AgentRepository agentRepository;



    /**
     * Test save and find agent task summary.
     */
    @Test
    void testSaveAndFindAgentTaskSummary() {
        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "8787879879", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentTaskSummaryEntity summary = new AgentTaskSummaryEntity(null, 10l, // taskId
                agent1.getId(), // agentId
                "Pickup Order 123", "PICKUP", "NEW", LocalDateTime.now(), "Customer Address A", "Store Address B", 10l);
        AgentTaskSummaryEntity savedSummary = agentTaskSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getTaskId()).isEqualTo(10l);
        assertThat(savedSummary.getAgentId()).isEqualTo(agent1.getId()); // Assert agentId

        Optional<AgentTaskSummaryEntity> foundSummary = agentTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskName()).isEqualTo("Pickup Order 123");
        assertThat(foundSummary.get().getAgentId()).isEqualTo(agent1.getId()); // Re-assert agentId
    }

    /**
     * Test update agent task summary.
     */
    @Test
    void testUpdateAgentTaskSummary() {
        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "8787879879", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentTaskSummaryEntity summary = new AgentTaskSummaryEntity(null, 10l, // taskId
                agent1.getId(), // agentId
                "Process Order 456", "PROCESS", "STARTED", LocalDateTime.now(), "Facility A", "Facility B", 10l);
        summary = agentTaskSummaryRepository.save(summary);

        AgentTaskSummaryEntity foundSummary = agentTaskSummaryRepository.findById(summary.getId()).orElseThrow();
        foundSummary.setTaskStatus("COMPLETED");
        foundSummary.setTaskName("Completed Order 456");
        agentTaskSummaryRepository.save(foundSummary);

        Optional<AgentTaskSummaryEntity> updatedSummary = agentTaskSummaryRepository.findById(foundSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getTaskStatus()).isEqualTo("COMPLETED");
        assertThat(updatedSummary.get().getTaskName()).isEqualTo("Completed Order 456");
    }

    /**
     * Test delete agent task summary.
     */
    @Test
    void testDeleteAgentTaskSummary() {
        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "8787879879", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentTaskSummaryEntity summary = new AgentTaskSummaryEntity(null, 10l, // taskId
                agent1.getId(), // agentId
                "Delivery Order 789", "DELIVERY", "ASSIGNED", LocalDateTime.now(), "Store C", "Customer D", 10l);
        summary=agentTaskSummaryRepository.save(summary);

        agentTaskSummaryRepository.deleteById(summary.getId());
        Optional<AgentTaskSummaryEntity> deletedSummary = agentTaskSummaryRepository.findById(summary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    /**
     * Test find by agent id.
     */
    @Test
    void testFindByAgentId() {
        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "8787879879", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, 10l, "9876543210", "A002", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated

        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, 10l, agent1.getId(), "Task for Agent 20-A", "PICKUP", "NEW", LocalDateTime.now(), "Addr E", "Dest F", 10l));
        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, 20l, agent1.getId(), "Task for Agent 20-B", "DELIVERY", "ASSIGNED", LocalDateTime.now(), "Addr G", "Dest H", 20l));
        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, 30l, agent2.getId(), "Task for Agent 21-A", "PROCESS", "STARTED", LocalDateTime.now(), "Addr I", "Dest J", 20l));

        List<AgentTaskSummaryEntity> tasksForAgent20 = agentTaskSummaryRepository.findByAgentId(agent1.getId());
        assertThat(tasksForAgent20).hasSize(2);
        assertThat(tasksForAgent20.stream().allMatch(s -> s.getAgentId().equals(agent1.getId()))).isTrue();
    }

    /**
     * Test find by task status.
     */
    @Test
    void testFindByTaskStatus() {

        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, 10l, "876876868", "A003", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, 10l, "987987987", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent3 = agentRepository.save(new AgentEntity(null, "Agent Sigma", AgentState.ACTIVE, 10l, "9878768766", "A005", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated



        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, 10l, agent1.getId(), "Task New 1", "PICKUP", "NEW", LocalDateTime.now(), "Addr K", "Dest L", 10l));
        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, 20l, agent2.getId(), "Task New 2", "DELIVERY", "NEW", LocalDateTime.now(), "Addr M", "Dest N", 20l));
        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, 30l, agent3.getId(), "Task Assigned 1", "PROCESS", "ASSIGNED", LocalDateTime.now(), "Addr O", "Dest P", 30l));

        List<AgentTaskSummaryEntity> newTasks = agentTaskSummaryRepository.findByTaskStatus("NEW");
        assertThat(newTasks).hasSize(2);
        assertThat(newTasks.stream().allMatch(s -> s.getTaskStatus().equals("NEW"))).isTrue();
    }
}
