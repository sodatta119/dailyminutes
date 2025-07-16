package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskTeamSummaryEntity;
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
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.task.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.task.domain.model") // Updated package name
class TaskTeamSummaryRepositoryTest {

    @Autowired
    private TaskTeamSummaryRepository taskTeamSummaryRepository;

    @Autowired
    private TaskRepository taskRepository;

    private String generateUniqueTeamName() {
        return "Team-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    void testSaveAndFindTaskTeamSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String teamName = generateUniqueTeamName();

        TaskTeamSummaryEntity summary = new TaskTeamSummaryEntity(null, task.getId(), 10l, teamName, "Team for pickup tasks", "FLEET");
        TaskTeamSummaryEntity savedSummary = taskTeamSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getTaskId()).isEqualTo(task.getId());
        assertThat(savedSummary.getTeamId()).isEqualTo(10l);
        assertThat(savedSummary.getTeamName()).isEqualTo(teamName);
        assertThat(savedSummary.getTeamRole()).isEqualTo("FLEET");

        Optional<TaskTeamSummaryEntity> foundSummary = taskTeamSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTeamName()).isEqualTo(teamName);
    }

    @Test
    void testUpdateTaskTeamSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String teamName = generateUniqueTeamName();

        TaskTeamSummaryEntity summary = new TaskTeamSummaryEntity(null, task.getId(), 10l, teamName, "Team for delivery tasks", "DELIVERY");
        TaskTeamSummaryEntity savedSummary = taskTeamSummaryRepository.save(summary);

        savedSummary.setTeamRole("OPS");
        savedSummary.setTeamDescription("Operations team for task management");
        taskTeamSummaryRepository.save(savedSummary);

        Optional<TaskTeamSummaryEntity> updatedSummary = taskTeamSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getTeamRole()).isEqualTo("OPS");
        assertThat(updatedSummary.get().getTeamDescription()).isEqualTo("Operations team for task management");
    }

    @Test
    void testDeleteTaskTeamSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String teamName = generateUniqueTeamName();

        TaskTeamSummaryEntity summary = new TaskTeamSummaryEntity(null, task.getId(), 10l, teamName, "Team to be deleted", "SUPPORT");
        TaskTeamSummaryEntity savedSummary = taskTeamSummaryRepository.save(summary);

        taskTeamSummaryRepository.deleteById(savedSummary.getId());
        Optional<TaskTeamSummaryEntity> deletedSummary = taskTeamSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByTaskId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskTeamSummaryRepository.save(new TaskTeamSummaryEntity(null, task1.getId(), 10l, generateUniqueTeamName(), "Team A", "FLEET"));
        taskTeamSummaryRepository.save(new TaskTeamSummaryEntity(null, task2.getId(), 20l, generateUniqueTeamName(), "Team B", "OPS"));

        Optional<TaskTeamSummaryEntity> foundSummary = taskTeamSummaryRepository.findByTaskId(task1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTeamRole()).isEqualTo("FLEET");
    }

    @Test
    void testFindByTeamId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskTeamSummaryRepository.save(new TaskTeamSummaryEntity(null, task1.getId(), 10l, generateUniqueTeamName(), "Team C", "FLEET"));
        taskTeamSummaryRepository.save(new TaskTeamSummaryEntity(null, task2.getId(), 10l, generateUniqueTeamName(), "Team D", "OPS"));
        taskTeamSummaryRepository.save(new TaskTeamSummaryEntity(null, task3.getId(), 20l, generateUniqueTeamName(), "Team E", "SUPPORT"));

        List<TaskTeamSummaryEntity> summaries = taskTeamSummaryRepository.findByTeamId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getTeamId().equals(10l))).isTrue();
    }

    @Test
    void testFindByTeamRole() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));

        taskTeamSummaryRepository.save(new TaskTeamSummaryEntity(null, task1.getId(), 10l, generateUniqueTeamName(), "Team F", "FLEET"));
        taskTeamSummaryRepository.save(new TaskTeamSummaryEntity(null, task2.getId(), 20l, generateUniqueTeamName(), "Team G", "FLEET"));
        taskTeamSummaryRepository.save(new TaskTeamSummaryEntity(null, task3.getId(), 30l, generateUniqueTeamName(), "Team H", "OPS"));

        List<TaskTeamSummaryEntity> fleetTeams = taskTeamSummaryRepository.findByTeamRole("FLEET");
        assertThat(fleetTeams).hasSize(2);
        assertThat(fleetTeams.stream().allMatch(s -> s.getTeamRole().equals("FLEET"))).isTrue();
    }
}
