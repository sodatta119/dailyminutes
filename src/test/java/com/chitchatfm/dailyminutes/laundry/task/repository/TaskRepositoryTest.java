package com.chitchatfm.dailyminutes.laundry.task.repository;

import com.chitchatfm.dailyminutes.DailyminutesApplication; // Import the main application class
import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.chitchatfm.dailyminutes.laundry.agent.domain.model.AgentState;
import com.chitchatfm.dailyminutes.laundry.agent.repository.AgentRepository;
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
 * The type Task repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.chitchatfm.dailyminutes.laundry.task.repository",
        "com.chitchatfm.dailyminutes.laundry.order.repository",
        "com.chitchatfm.dailyminutes.laundry.agent.repository",
        "com.chitchatfm.dailyminutes.laundry.store.repository",
        "com.chitchatfm.dailyminutes.laundry.team.repository",
        "com.chitchatfm.dailyminutes.laundry.customer.repository"})
@ComponentScan(basePackages = {"com.chitchatfm.dailyminutes.laundry.task.domain.model",
        "com.chitchatfm.dailyminutes.laundry.order.domain.model",
        "com.chitchatfm.dailyminutes.laundry.agent.domain.model",
        "com.chitchatfm.dailyminutes.laundry.store.domain.model",
        "com.chitchatfm.dailyminutes.laundry.team.domain.model",
        "com.chitchatfm.dailyminutes.laundry.customer.domain.model"})
class TaskRepositoryTest {


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

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
     * Test save and find task.
     */
    @Test
    void testSaveAndFindTask() {
        TaskEntity task = new TaskEntity(null, "Pickup Order 1", "Collect laundry from customer", TaskType.PICKUP,
                LocalDateTime.now(), null, null, TaskStatus.NEW,
                team.getId(), agent.getId(), "Customer Address 1", null, "Store Address 1", null, "Call customer 15 mins prior", order.getId());
        TaskEntity savedTask = taskRepository.save(task);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();

        Optional<TaskEntity> foundTask = taskRepository.findById(savedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getName()).isEqualTo("Pickup Order 1");
        assertThat(foundTask.get().getOrderId()).isEqualTo(order.getId());
    }

    /**
     * Test update task.
     */
    @Test
    void testUpdateTask() {
        TaskEntity task = new TaskEntity(null, "Process Order 2", "Wash and iron clothes", TaskType.PROCESS,
                LocalDateTime.now(), null, null, TaskStatus.NEW,
                team.getId(), agent.getId(), "Laundry Facility", null, "Customer Address 2", null, "Handle with care", order.getId());
        TaskEntity savedTask = taskRepository.save(task);

        savedTask.setStatus(TaskStatus.STARTED);
        savedTask.setTaskUpdatedTime(LocalDateTime.now());
        savedTask.setTaskComment("Started processing at 10:00 AM");
        TaskEntity updatedTask = taskRepository.save(savedTask);

        Optional<TaskEntity> foundUpdatedTask = taskRepository.findById(updatedTask.getId());
        assertThat(foundUpdatedTask).isPresent();
        assertThat(foundUpdatedTask.get().getStatus()).isEqualTo(TaskStatus.STARTED);
        assertThat(foundUpdatedTask.get().getTaskComment()).isEqualTo("Started processing at 10:00 AM");
    }

    /**
     * Test delete task.
     */
    @Test
    void testDeleteTask() {
        TaskEntity task = new TaskEntity(null, "Delivery Order 3", "Deliver clean clothes", TaskType.DELIVERY,
                LocalDateTime.now(), null, null, TaskStatus.ASSIGNED,
                team.getId(), agent.getId(), "Store Address 3", null, "Customer Address 3", null, "Confirm delivery with OTP", order.getId());
        TaskEntity savedTask = taskRepository.save(task);

        taskRepository.deleteById(savedTask.getId());
        Optional<TaskEntity> deletedTask = taskRepository.findById(savedTask.getId());
        assertThat(deletedTask).isNotPresent();
    }

    /**
     * Test find by order id.
     */
    @Test
    void testFindByOrderId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("20.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("15.50")));

        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, team.getId(), "876876868", "A003", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, team.getId(), "987987987", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent3 = agentRepository.save(new AgentEntity(null, "Agent Sigma", AgentState.ACTIVE, team.getId(), "767657676", "A005", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated

        taskRepository.save(new TaskEntity(null, "Task A", "Desc A", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent1.getId(), "Addr A", null, "Dest A", null, "Comment A", order1.getId()));
        taskRepository.save(new TaskEntity(null, "Task B", "Desc B", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, team.getId(), agent2.getId(), "Addr B", null, "Dest B", null, "Comment B", order1.getId()));
        taskRepository.save(new TaskEntity(null, "Task C", "Desc C", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.STARTED, team.getId(), agent3.getId(), "Addr C", null, "Dest C", null, "Comment C", order3.getId()));

        List<TaskEntity> tasks = taskRepository.findByOrderId(order1.getId());
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getOrderId()).isEqualTo(order1.getId());
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
        AgentEntity agent3 = agentRepository.save(new AgentEntity(null, "Agent Sigma", AgentState.ACTIVE, team.getId(), "767657676", "A005", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated

        taskRepository.save(new TaskEntity(null, "Task D", "Desc D", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, team.getId(), agent1.getId(), "Addr D", null, "Dest D", null, "Comment D", order1.getId()));
        taskRepository.save(new TaskEntity(null, "Task E", "Desc E", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.STARTED, team.getId(), agent1.getId(), "Addr E", null, "Dest E", null, "Comment E", order2.getId()));
        taskRepository.save(new TaskEntity(null, "Task F", "Desc F", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), agent2.getId(), "Addr F", null, "Dest F", null, "Comment F", order3.getId()));

        List<TaskEntity> tasks = taskRepository.findByAgentId(agent1.getId());
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getAgentId()).isEqualTo(agent1.getId());
    }

    /**
     * Test find by team id.
     */
    @Test
    void testFindByTeamId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("20.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("15.50")));

        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, team.getId(), "876876868", "A003", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, team.getId(), "987987987", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent3 = agentRepository.save(new AgentEntity(null, "Agent Sigma", AgentState.ACTIVE, team.getId(), "767657676", "A005", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated

        taskRepository.save(new TaskEntity(null, "Task G", "Desc G", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, team.getId(), null, "Addr G", null, "Dest G", null, "Comment G", order1.getId()));
        taskRepository.save(new TaskEntity(null, "Task H", "Desc H", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, team.getId(), null, "Addr H", null, "Dest H", null, "Comment H", order2.getId()));
        taskRepository.save(new TaskEntity(null, "Task I", "Desc I", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.STARTED, team.getId(), null, "Addr I", null, "Dest I", null, "Comment I", order3.getId()));

        List<TaskEntity> tasks = taskRepository.findByTeamId(team.getId());
        assertThat(tasks).hasSize(4);
        assertThat(tasks.get(0).getTeamId()).isEqualTo(team.getId());
    }

    /**
     * Test find by status.
     */
    @Test
    void testFindByStatus() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("20.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("15.50")));

        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, team.getId(), "876876868", "A003", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, team.getId(), "987987987", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent3 = agentRepository.save(new AgentEntity(null, "Agent Sigma", AgentState.ACTIVE, team.getId(), "767657676", "A005", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated

        taskRepository.save(new TaskEntity(null, "Task J", "Desc J", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr J", null, "Dest J", null, "Comment J", order1.getId()));
        taskRepository.save(new TaskEntity(null, "Task K", "Desc K", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr K", null, "Dest K", null, "Comment K", order2.getId()));
        taskRepository.save(new TaskEntity(null, "Task L", "Desc L", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, null, null, "Addr L", null, "Dest L", null, "Comment L", order3.getId()));

        List<TaskEntity> tasks = taskRepository.findByStatus(TaskStatus.NEW);
        assertThat(tasks).hasSize(3);
        assertThat(tasks.get(0).getStatus()).isEqualTo(TaskStatus.NEW);
    }

    /**
     * Test find by type.
     */
    @Test
    void testFindByType() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("20.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("15.50")));

        AgentEntity agent1 = agentRepository.save(new AgentEntity(null, "Agent Alpha", AgentState.ACTIVE, team.getId(), "876876868", "A003", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent2 = agentRepository.save(new AgentEntity(null, "Agent Beta", AgentState.ACTIVE, team.getId(), "987987987", "A004", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated
        AgentEntity agent3 = agentRepository.save(new AgentEntity(null, "Agent Sigma", AgentState.ACTIVE, team.getId(), "767657676", "A005", LocalDate.now(), null, AgentDesignation.FLEET_AGENT)); // Updated

        taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr M", null, "Dest M", null, "Comment M", order1.getId()));
        taskRepository.save(new TaskEntity(null, "Task N", "Desc N", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, null, null, "Addr N", null, "Dest N", null, "Comment N", order2.getId()));
        taskRepository.save(new TaskEntity(null, "Task O", "Desc O", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.STARTED, null, null, "Addr O", null, "Dest O", null, "Comment O", order3.getId()));

        List<TaskEntity> tasks = taskRepository.findByType(TaskType.PICKUP);
        assertThat(tasks).hasSize(3);
        assertThat(tasks.get(0).getType()).isEqualTo(TaskType.PICKUP);
    }

    /**
     * Test find by name.
     */
    @Test
    void testFindByName() {
        taskRepository.save(new TaskEntity(null, "Specific Task Name", "A very specific task", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr X", null, "Dest X", null, "Comment X", order.getId()));
        Optional<TaskEntity> foundTask = taskRepository.findByName("Specific Task Name");
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getType()).isEqualTo(TaskType.PROCESS);
    }
}
