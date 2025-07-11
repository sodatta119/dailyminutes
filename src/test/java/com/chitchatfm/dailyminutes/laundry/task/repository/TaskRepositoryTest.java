package com.chitchatfm.dailyminutes.laundry.task.repository;

import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.chitchatfm.dailyminutes.laundry.task.domain.model.TaskType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Task repository test.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Test save and find task.
     */
    @Test
    void testSaveAndFindTask() {
        TaskEntity task = new TaskEntity(null, "Pickup Order 1", "Collect laundry from customer", TaskType.PICKUP,
                LocalDateTime.now(), null, null, TaskStatus.NEW,
                null, null, "Customer Address 1", null, "Store Address 1", null, "Call customer 15 mins prior", 1L);
        TaskEntity savedTask = taskRepository.save(task);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();

        Optional<TaskEntity> foundTask = taskRepository.findById(savedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getName()).isEqualTo("Pickup Order 1");
        assertThat(foundTask.get().getOrderId()).isEqualTo(1L);
    }

    /**
     * Test find by order id.
     */
    @Test
    void testFindByOrderId() {
        taskRepository.save(new TaskEntity(null, "Task A", "Desc A", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr A", null, "Dest A", null, "Comment A", 100L));
        taskRepository.save(new TaskEntity(null, "Task B", "Desc B", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, null, null, "Addr B", null, "Dest B", null, "Comment B", 100L));
        taskRepository.save(new TaskEntity(null, "Task C", "Desc C", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.STARTED, null, null, "Addr C", null, "Dest C", null, "Comment C", 200L));

        List<TaskEntity> tasks = taskRepository.findByOrderId(100L);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getOrderId()).isEqualTo(100L);
    }

    /**
     * Test find by agent id.
     */
    @Test
    void testFindByAgentId() {
        taskRepository.save(new TaskEntity(null, "Task D", "Desc D", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, null, 1L, "Addr D", null, "Dest D", null, "Comment D", 300L));
        taskRepository.save(new TaskEntity(null, "Task E", "Desc E", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.STARTED, null, 1L, "Addr E", null, "Dest E", null, "Comment E", 400L));
        taskRepository.save(new TaskEntity(null, "Task F", "Desc F", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.NEW, null, 2L, "Addr F", null, "Dest F", null, "Comment F", 500L));

        List<TaskEntity> tasks = taskRepository.findByAgentId(1L);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getAgentId()).isEqualTo(1L);
    }

    /**
     * Test find by team id.
     */
    @Test
    void testFindByTeamId() {
        taskRepository.save(new TaskEntity(null, "Task G", "Desc G", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10L, null, "Addr G", null, "Dest G", null, "Comment G", 600L));
        taskRepository.save(new TaskEntity(null, "Task H", "Desc H", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, 10L, null, "Addr H", null, "Dest H", null, "Comment H", 700L));
        taskRepository.save(new TaskEntity(null, "Task I", "Desc I", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.STARTED, 20L, null, "Addr I", null, "Dest I", null, "Comment I", 800L));

        List<TaskEntity> tasks = taskRepository.findByTeamId(10L);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getTeamId()).isEqualTo(10L);
    }

    /**
     * Test find by status.
     */
    @Test
    void testFindByStatus() {
        taskRepository.save(new TaskEntity(null, "Task J", "Desc J", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr J", null, "Dest J", null, "Comment J", 900L));
        taskRepository.save(new TaskEntity(null, "Task K", "Desc K", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr K", null, "Dest K", null, "Comment K", 901L));
        taskRepository.save(new TaskEntity(null, "Task L", "Desc L", TaskType.PROCESS, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, null, null, "Addr L", null, "Dest L", null, "Comment L", 902L));

        List<TaskEntity> tasks = taskRepository.findByStatus(TaskStatus.NEW);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getStatus()).isEqualTo(TaskStatus.NEW);
    }

    /**
     * Test find by type.
     */
    @Test
    void testFindByType() {
        taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, null, null, "Addr M", null, "Dest M", null, "Comment M", 1000L));
        taskRepository.save(new TaskEntity(null, "Task N", "Desc N", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, null, null, "Addr N", null, "Dest N", null, "Comment N", 1001L));
        taskRepository.save(new TaskEntity(null, "Task O", "Desc O", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.STARTED, null, null, "Addr O", null, "Dest O", null, "Comment O", 1002L));

        List<TaskEntity> tasks = taskRepository.findByType(TaskType.PICKUP);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getType()).isEqualTo(TaskType.PICKUP);
    }
}

