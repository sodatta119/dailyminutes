package com.dailyminutes.laundry.task.repository;

import com.dailyminutes.DailyminutesApplication;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Task repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.task.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.task.domain.model"})
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Test save and find task.
     */
    @Test
    void testSaveAndFindTask() {
        TaskEntity task = new TaskEntity(null, null,"Pickup Order 1", "Collect laundry from customer", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 20l, "Customer Address 1", null, "Store Address 1", null, "Call customer 15 mins prior", 30l);
        TaskEntity savedTask = taskRepository.save(task);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();

        Optional<TaskEntity> foundTask = taskRepository.findById(savedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getName()).isEqualTo("Pickup Order 1");
        assertThat(foundTask.get().getOrderId()).isEqualTo(30l);
    }

    /**
     * Test update task.
     */
    @Test
    void testUpdateTask() {
        TaskEntity task = new TaskEntity(null,null, "Process Order 2", "Wash and iron clothes", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 20l, "Laundry Facility", null, "Customer Address 2", null, "Handle with care", 30l);
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
        TaskEntity task = new TaskEntity(null, null,"Delivery Order 3", "Deliver clean clothes", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, 10l, 20l, "Store Address 3", null, "Customer Address 3", null, "Confirm delivery with OTP", 30l);
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
        taskRepository.save(new TaskEntity(null, null,"Task A", "Desc A", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr A", null, "Dest A", null, "Comment A", 10l));
        taskRepository.save(new TaskEntity(null, null,"Task B", "Desc B", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, 10l, 20l, "Addr B", null, "Dest B", null, "Comment B", 10l));
        taskRepository.save(new TaskEntity(null, null,"Task C", "Desc C", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.STARTED, 10l, 30l, "Addr C", null, "Dest C", null, "Comment C", 20l));

        List<TaskEntity> tasks = taskRepository.findByOrderId(10l);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getOrderId()).isEqualTo(10l);
    }

    /**
     * Test find by agent id.
     */
    @Test
    void testFindByAgentId() {
        taskRepository.save(new TaskEntity(null, null,"Task D", "Desc D", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, 10l, 10l, "Addr D", null, "Dest D", null, "Comment D", 10l));
        taskRepository.save(new TaskEntity(null, null,"Task E", "Desc E", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.STARTED, 20l, 10l, "Addr E", null, "Dest E", null, "Comment E", 20l));
        taskRepository.save(new TaskEntity(null, null,"Task F", "Desc F", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.NEW, 30l, 30l, "Addr F", null, "Dest F", null, "Comment F", 30l));

        List<TaskEntity> tasks = taskRepository.findByAgentId(10l);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getAgentId()).isEqualTo(10l);
    }

    /**
     * Test find by team id.
     */
    @Test
    void testFindByTeamId() {
        taskRepository.save(new TaskEntity(null, null,"Task G", "Desc G", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, null, "Addr G", null, "Dest G", null, "Comment G", 10l));
        taskRepository.save(new TaskEntity(null, null,"Task H", "Desc H", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, 10l, null, "Addr H", null, "Dest H", null, "Comment H", 20l));
        taskRepository.save(new TaskEntity(null, null,"Task I", "Desc I", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.STARTED, 10l, null, "Addr I", null, "Dest I", null, "Comment I", 30l));

        List<TaskEntity> tasks = taskRepository.findByTeamId(10l);
        assertThat(tasks).hasSize(3);
        assertThat(tasks.get(0).getTeamId()).isEqualTo(10l);
    }

    /**
     * Test find by status.
     */
    @Test
    void testFindByStatus() {
        taskRepository.save(new TaskEntity(null, null,"Task J", "Desc J", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr J", null, "Dest J", null, "Comment J", 10l));
        taskRepository.save(new TaskEntity(null, null,"Task K", "Desc K", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr K", null, "Dest K", null, "Comment K", 20l));
        taskRepository.save(new TaskEntity(null, null,"Task L", "Desc L", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, null, null, "Addr L", null, "Dest L", null, "Comment L", 30l));

        List<TaskEntity> tasks = taskRepository.findByStatus(TaskStatus.NEW);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getStatus()).isEqualTo(TaskStatus.NEW);
    }

    /**
     * Test find by type.
     */
    @Test
    void testFindByType() {
        taskRepository.save(new TaskEntity(null, null,"Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskRepository.save(new TaskEntity(null, null,"Task N", "Desc N", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, null, null, "Addr N", null, "Dest N", null, "Comment N", 20l));
        taskRepository.save(new TaskEntity(null, null,"Task O", "Desc O", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.STARTED, null, null, "Addr O", null, "Dest O", null, "Comment O", 30l));

        List<TaskEntity> tasks = taskRepository.findByType(TaskType.PICKUP);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getType()).isEqualTo(TaskType.PICKUP);
    }

    /**
     * Test find by name.
     */
    @Test
    void testFindByName() {
        taskRepository.save(new TaskEntity(null, null,"Specific Task Name", "A very specific task", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr X", null, "Dest X", null, "Comment X", 10l));
        Optional<TaskEntity> foundTask = taskRepository.findByName("Specific Task Name");
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getType()).isEqualTo(TaskType.PROCESS);
    }
}
