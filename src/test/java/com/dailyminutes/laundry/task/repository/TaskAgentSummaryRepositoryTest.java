package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.task.domain.model.TaskAgentSummaryEntity;
import com.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskType;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 12/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.task.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.task.domain.model") // Updated package name
class TaskAgentSummaryRepositoryTest {

    @Autowired
    private TaskAgentSummaryRepository taskAgentSummaryRepository;

    @Autowired
    private TaskRepository taskRepository;

    private String generateUniquePhoneNumber() {
        return "9" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    @Test
    void testSaveAndFindTaskAgentSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String phoneNumber = generateUniquePhoneNumber();

        TaskAgentSummaryEntity summary = new TaskAgentSummaryEntity(null, task.getId(), 10l, "Agent Alpha", phoneNumber, "FLEET_AGENT", "ACTIVE");
        TaskAgentSummaryEntity savedSummary = taskAgentSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getTaskId()).isEqualTo(task.getId());
        assertThat(savedSummary.getAgentId()).isEqualTo(10l);
        assertThat(savedSummary.getAgentName()).isEqualTo("Agent Alpha");
        assertThat(savedSummary.getAgentPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedSummary.getAgentDesignation()).isEqualTo("FLEET_AGENT");
        assertThat(savedSummary.getAgentState()).isEqualTo("ACTIVE");

        Optional<TaskAgentSummaryEntity> foundSummary = taskAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAgentName()).isEqualTo("Agent Alpha");
    }

    @Test
    void testUpdateTaskAgentSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String phoneNumber = generateUniquePhoneNumber();

        TaskAgentSummaryEntity summary = new TaskAgentSummaryEntity(null, task.getId(), 10l, "Agent Beta", phoneNumber, "DELIVERY_EXECUTIVE", "ACTIVE");
        TaskAgentSummaryEntity savedSummary = taskAgentSummaryRepository.save(summary);

        savedSummary.setAgentState("INACTIVE");
        savedSummary.setAgentName("Agent Beta (Inactive)");
        taskAgentSummaryRepository.save(savedSummary);

        Optional<TaskAgentSummaryEntity> updatedSummary = taskAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getAgentState()).isEqualTo("INACTIVE");
        assertThat(updatedSummary.get().getAgentName()).isEqualTo("Agent Beta (Inactive)");
    }

    @Test
    void testDeleteTaskAgentSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String phoneNumber = generateUniquePhoneNumber();

        TaskAgentSummaryEntity summary = new TaskAgentSummaryEntity(null, task.getId(), 10l, "Agent Gamma", phoneNumber, "PROCESS_MANAGER", "ACTIVE");
        TaskAgentSummaryEntity savedSummary = taskAgentSummaryRepository.save(summary);

        taskAgentSummaryRepository.deleteById(savedSummary.getId());
        Optional<TaskAgentSummaryEntity> deletedSummary = taskAgentSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByTaskId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task1.getId(), 10l, "Agent D", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task2.getId(), 20l, "Agent E", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));

        Optional<TaskAgentSummaryEntity> foundSummary = taskAgentSummaryRepository.findByTaskId(task1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAgentName()).isEqualTo("Agent D");
    }

    @Test
    void testFindByAgentId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task1.getId(), 10l, "Agent F", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task2.getId(), 20l, "Agent G", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));

        Optional<TaskAgentSummaryEntity> foundSummary = taskAgentSummaryRepository.findByAgentId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAgentName()).isEqualTo("Agent F");
    }

    @Test
    void testFindByAgentDesignation() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task1.getId(), 10l, "Agent H", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task2.getId(), 20l, "Agent I", generateUniquePhoneNumber(), "FLEET_AGENT", "INACTIVE"));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task3.getId(), 30l, "Agent J", generateUniquePhoneNumber(), "PROCESS_MANAGER", "ACTIVE"));

        List<TaskAgentSummaryEntity> fleetAgents = taskAgentSummaryRepository.findByAgentDesignation("FLEET_AGENT");
        assertThat(fleetAgents).hasSize(2);
        assertThat(fleetAgents.stream().allMatch(s -> s.getAgentDesignation().equals("FLEET_AGENT"))).isTrue();
    }

    @Test
    void testFindByAgentState() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task1.getId(), 10l, "Agent K", generateUniquePhoneNumber(), "FLEET_AGENT", "ACTIVE"));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task2.getId(), 20l, "Agent L", generateUniquePhoneNumber(), "DELIVERY_EXECUTIVE", "ACTIVE"));
        taskAgentSummaryRepository.save(new TaskAgentSummaryEntity(null, task3.getId(), 30l, "Agent M", generateUniquePhoneNumber(), "PROCESS_MANAGER", "INACTIVE"));

        List<TaskAgentSummaryEntity> activeAgents = taskAgentSummaryRepository.findByAgentState("ACTIVE");
        assertThat(activeAgents).hasSize(2);
        assertThat(activeAgents.stream().allMatch(s -> s.getAgentState().equals("ACTIVE"))).isTrue();
    }
}
