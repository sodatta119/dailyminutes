/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
package com.chitchatfm.dailyminutes.laundry.agent.repository;

import com.chitchatfm.dailyminutes.DailyminutesApplication;
import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentState;
import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentTaskSummaryEntity;
import com.chitchatfm.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.chitchatfm.dailyminutes.laundry.customer.repository.CustomerRepository;
import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.chitchatfm.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.chitchatfm.dailyminutes.laundry.order.repository.OrderRepository;
import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.chitchatfm.dailyminutes.laundry.store.repository.StoreRepository;
import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskType;
import com.chitchatfm.dailyminutes.laundry.task.repository.TaskRepository;
import com.chitchatfm.dailyminutes.laundry.team.domain.model.TeamEntity;
import com.chitchatfm.dailyminutes.laundry.team.domain.model.TeamRole;
import com.chitchatfm.dailyminutes.laundry.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Agent task summary repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.chitchatfm.dailyminutes.laundry.agent.repository", "com.chitchatfm.dailyminutes.laundry.task.repository", "com.chitchatfm.dailyminutes.laundry.team.repository", "com.chitchatfm.dailyminutes.laundry.store.repository", "com.chitchatfm.dailyminutes.laundry.customer.repository", "com.chitchatfm.dailyminutes.laundry.order.repository"})
@ComponentScan(basePackages = {"com.chitchatfm.dailyminutes.laundry.agent.domain.model", "com.chitchatfm.dailyminutes.laundry.task.domain.model", "com.chitchatfm.dailyminutes.laundry.team.domain.model", "com.chitchatfm.dailyminutes.laundry.store.domain.model", "com.chitchatfm.dailyminutes.laundry.customer.domain.model", "com.chitchatfm.dailyminutes.laundry.order.domain.model"})
class AgentTaskSummaryRepositoryTest {

    @Autowired
    private AgentTaskSummaryRepository agentTaskSummaryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AgentRepository agentRepository;

    /**
     * The Store.
     */
    StoreEntity store;
    /**
     * The Customer.
     */
    CustomerEntity customer;

    /**
     * The Order.
     */
    OrderEntity order;

    /**
     * The Team.
     */
    TeamEntity team;
    /**
     * The Agent.
     */
    AgentEntity agent;
    /**
     * The Task.
     */
    TaskEntity task;


    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        this.store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        this.customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        this.order = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        this.team = teamRepository.save(new TeamEntity(null, "Fleet Team X", "Vehicle management", TeamRole.FLEET));
        this.agent = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, team.getId(), "9876543210", "A001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        this.task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent.getId(), "Addr M", null, "Dest M", null, "Comment M", order.getId()));
    }

    /**
     * Test save and find agent task summary.
     */
    @Test
    void testSaveAndFindAgentTaskSummary() {
        AgentTaskSummaryEntity summary = new AgentTaskSummaryEntity(null, task.getId(), // taskId
                agent.getId(), // agentId
                "Pickup Order 123", "PICKUP", "NEW", LocalDateTime.now(), "Customer Address A", "Store Address B", order.getId());
        AgentTaskSummaryEntity savedSummary = agentTaskSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getTaskId()).isEqualTo(task.getId());
        assertThat(savedSummary.getAgentId()).isEqualTo(agent.getId()); // Assert agentId

        Optional<AgentTaskSummaryEntity> foundSummary = agentTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskName()).isEqualTo("Pickup Order 123");
        assertThat(foundSummary.get().getAgentId()).isEqualTo(agent.getId()); // Re-assert agentId
    }

    /**
     * Test update agent task summary.
     */
    @Test
    void testUpdateAgentTaskSummary() {
        AgentTaskSummaryEntity summary = new AgentTaskSummaryEntity(null, task.getId(), // taskId
                agent.getId(), // agentId
                "Process Order 456", "PROCESS", "STARTED", LocalDateTime.now(), "Facility A", "Facility B", order.getId());
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
        AgentTaskSummaryEntity summary = new AgentTaskSummaryEntity(null, task.getId(), // taskId
                agent.getId(), // agentId
                "Delivery Order 789", "DELIVERY", "ASSIGNED", LocalDateTime.now(), "Store C", "Customer D", order.getId());
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
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("20.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("15.50")));

        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, team.getId(), "876876868", "A003", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, team.getId(), "987987987", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent1.getId(), "Addr M", null, "Dest M", null, "Comment M", order1.getId()));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task N", "Desc N", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent1.getId(), "Addr N", null, "Dest N", null, "Comment N", order2.getId()));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task O", "Desc O", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent2.getId(), "Addr O", null, "Dest O", null, "Comment O", order3.getId()));


        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, task1.getId(), agent1.getId(), "Task for Agent 20-A", "PICKUP", "NEW", LocalDateTime.now(), "Addr E", "Dest F", order1.getId()));
        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, task2.getId(), agent1.getId(), "Task for Agent 20-B", "DELIVERY", "ASSIGNED", LocalDateTime.now(), "Addr G", "Dest H", order2.getId()));
        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, task3.getId(), agent2.getId(), "Task for Agent 21-A", "PROCESS", "STARTED", LocalDateTime.now(), "Addr I", "Dest J", order2.getId()));

        List<AgentTaskSummaryEntity> tasksForAgent20 = agentTaskSummaryRepository.findByAgentId(agent1.getId());
        assertThat(tasksForAgent20).hasSize(2);
        assertThat(tasksForAgent20.stream().allMatch(s -> s.getAgentId().equals(agent1.getId()))).isTrue();
    }

    /**
     * Test find by task status.
     */
    @Test
    void testFindByTaskStatus() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("20.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("15.50")));

        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, team.getId(), "876876868", "A003", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, team.getId(), "987987987", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent3 = agentRepository.save(new AgentEntity(null, "Agent Sigma", AgentState.ACTIVE, team.getId(), "9878768766", "A005", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated

        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent1.getId(), "Addr M", null, "Dest M", null, "Comment M", order1.getId()));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task N", "Desc N", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent2.getId(), "Addr N", null, "Dest N", null, "Comment N", order2.getId()));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task O", "Desc O", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent3.getId(), "Addr O", null, "Dest O", null, "Comment O", order3.getId()));


        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, task1.getId(), agent1.getId(), "Task New 1", "PICKUP", "NEW", LocalDateTime.now(), "Addr K", "Dest L", order1.getId()));
        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, task2.getId(), agent2.getId(), "Task New 2", "DELIVERY", "NEW", LocalDateTime.now(), "Addr M", "Dest N", order2.getId()));
        agentTaskSummaryRepository.save(new AgentTaskSummaryEntity(null, task3.getId(), agent3.getId(), "Task Assigned 1", "PROCESS", "ASSIGNED", LocalDateTime.now(), "Addr O", "Dest P", order3.getId()));

        List<AgentTaskSummaryEntity> newTasks = agentTaskSummaryRepository.findByTaskStatus("NEW");
        assertThat(newTasks).hasSize(2);
        assertThat(newTasks.stream().allMatch(s -> s.getTaskStatus().equals("NEW"))).isTrue();
    }
}
