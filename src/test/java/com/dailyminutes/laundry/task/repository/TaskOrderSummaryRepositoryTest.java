package com.dailyminutes.laundry.task.repository;


import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.dailyminutes.laundry.task.domain.model.TaskOrderSummaryEntity;
import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.task.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.task.domain.model") // Updated package name
class TaskOrderSummaryRepositoryTest {

    @Autowired
    private TaskOrderSummaryRepository taskOrderSummaryRepository;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    void testSaveAndFindTaskOrderSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskOrderSummaryEntity summary = new TaskOrderSummaryEntity(
                null, task.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("150.00"), 10l, 10l);
        TaskOrderSummaryEntity savedSummary = taskOrderSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getTaskId()).isEqualTo(task.getId());
        assertThat(savedSummary.getOrderId()).isEqualTo(10l);
        assertThat(savedSummary.getCustomerId()).isEqualTo(10l);
        assertThat(savedSummary.getStoreId()).isEqualTo(10l);
        assertThat(savedSummary.getStatus()).isEqualTo("PENDING");

        Optional<TaskOrderSummaryEntity> foundSummary = taskOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalAmount()).isEqualByComparingTo("150.00");
    }

    @Test
    void testUpdateTaskOrderSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskOrderSummaryEntity summary = new TaskOrderSummaryEntity(
                null, task.getId(), 10l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("200.00"), 10l, 10l);
        TaskOrderSummaryEntity savedSummary = taskOrderSummaryRepository.save(summary);

        savedSummary.setStatus("DELIVERED");
        savedSummary.setTotalAmount(new BigDecimal("210.00"));
        taskOrderSummaryRepository.save(savedSummary);

        Optional<TaskOrderSummaryEntity> updatedSummary = taskOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStatus()).isEqualTo("DELIVERED");
        assertThat(updatedSummary.get().getTotalAmount()).isEqualByComparingTo("210.00");
    }

    @Test
    void testDeleteTaskOrderSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));

        TaskOrderSummaryEntity summary = new TaskOrderSummaryEntity(
                null, task.getId(), 10l, LocalDateTime.now(), "CANCELLED", new BigDecimal("50.00"), 10l, 10l);
        TaskOrderSummaryEntity savedSummary = taskOrderSummaryRepository.save(summary);

        taskOrderSummaryRepository.deleteById(savedSummary.getId());
        Optional<TaskOrderSummaryEntity> deletedSummary = taskOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByTaskId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task1.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("100.00"), 10l, 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task2.getId(), 20l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("50.00"), 20l, 20l));

        Optional<TaskOrderSummaryEntity> foundSummary = taskOrderSummaryRepository.findByTaskId(task1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("PENDING");
    }

    @Test
    void testFindByOrderId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task1.getId(), 10l, LocalDateTime.now(), "DELIVERED", new BigDecimal("70.00"), 10l, 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task2.getId(), 20l, LocalDateTime.now(), "IN_PROCESS", new BigDecimal("90.00"), 20l, 20l));

        Optional<TaskOrderSummaryEntity> foundSummary = taskOrderSummaryRepository.findByOrderId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("DELIVERED");
    }

    @Test
    void testFindByCustomerId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task1.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("110.00"), 10l, 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task2.getId(), 20l, LocalDateTime.now(), "COMPLETED", new BigDecimal("120.00"), 10l, 20l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task3.getId(), 30l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("130.00"), 30l, 30l));

        List<TaskOrderSummaryEntity> summaries = taskOrderSummaryRepository.findByCustomerId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getCustomerId().equals(10l))).isTrue();
    }

    @Test
    void testFindByStoreId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task1.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("140.00"), 10l, 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task2.getId(), 20l, LocalDateTime.now(), "COMPLETED", new BigDecimal("150.00"), 20l, 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task3.getId(), 30l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("160.00"), 30l, 30l));

        List<TaskOrderSummaryEntity> summaries = taskOrderSummaryRepository.findByStoreId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getStoreId().equals(10l))).isTrue();
    }

    @Test
    void testFindByStatus() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task1.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("25.00"), 10l, 10l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task2.getId(), 20l, LocalDateTime.now(), "PENDING", new BigDecimal("35.00"), 20l, 20l));
        taskOrderSummaryRepository.save(new TaskOrderSummaryEntity(null, task3.getId(), 30l, LocalDateTime.now(), "DELIVERED", new BigDecimal("45.00"), 30l, 30l));

        List<TaskOrderSummaryEntity> pendingSummaries = taskOrderSummaryRepository.findByStatus("PENDING");
        assertThat(pendingSummaries).hasSize(2);
        assertThat(pendingSummaries.stream().allMatch(s -> s.getStatus().equals("PENDING"))).isTrue();
    }
}
