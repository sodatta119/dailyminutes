package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.dailyminutes.laundry.task.domain.model.TaskGeofenceSummaryEntity;
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
 * The type Task geofence summary repository test.
 *
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16 /07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.task.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.task.domain.model") // Updated package name
class TaskGeofenceSummaryRepositoryTest {

    @Autowired
    private TaskGeofenceSummaryRepository taskGeofenceSummaryRepository;

    @Autowired
    private TaskRepository taskRepository;

    private String generateUniqueGeofenceName() {
        return "GF-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateCoordinates() {
        return "POLYGON((" + UUID.randomUUID().toString().substring(0, 4) + " 0, 1 0, 1 1, 0 1, 0 0))";
    }

    /**
     * Test save and find task geofence summary.
     */
    @Test
    void testSaveAndFindTaskGeofenceSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String geofenceName = generateUniqueGeofenceName();
        String coordinates = generateCoordinates();

        TaskGeofenceSummaryEntity summary = new TaskGeofenceSummaryEntity(null, task.getId(), 10l, geofenceName, "DELIVERY_ZONE", coordinates, true, false);
        TaskGeofenceSummaryEntity savedSummary = taskGeofenceSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getTaskId()).isEqualTo(task.getId());
        assertThat(savedSummary.getGeofenceId()).isEqualTo(10l);
        assertThat(savedSummary.getGeofenceName()).isEqualTo(geofenceName);
        assertThat(savedSummary.getGeofenceType()).isEqualTo("DELIVERY_ZONE");
        assertThat(savedSummary.getPolygonCoordinates()).isEqualTo(coordinates);
        assertThat(savedSummary.isSource()).isTrue();
        assertThat(savedSummary.isDestination()).isFalse();

        Optional<TaskGeofenceSummaryEntity> foundSummary = taskGeofenceSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getGeofenceName()).isEqualTo(geofenceName);
    }

    /**
     * Test update task geofence summary.
     */
    @Test
    void testUpdateTaskGeofenceSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String geofenceName = generateUniqueGeofenceName();
        String coordinates = generateCoordinates();

        TaskGeofenceSummaryEntity summary = new TaskGeofenceSummaryEntity(null, task.getId(), 10l, geofenceName, "PICKUP_ZONE", coordinates, false, true);
        TaskGeofenceSummaryEntity savedSummary = taskGeofenceSummaryRepository.save(summary);

        savedSummary.setGeofenceType("SERVICE_AREA");
        savedSummary.setSource(true);
        savedSummary.setDestination(false);
        taskGeofenceSummaryRepository.save(savedSummary);

        Optional<TaskGeofenceSummaryEntity> updatedSummary = taskGeofenceSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getGeofenceType()).isEqualTo("SERVICE_AREA");
        assertThat(updatedSummary.get().isSource()).isTrue();
        assertThat(updatedSummary.get().isDestination()).isFalse();
    }

    /**
     * Test delete task geofence summary.
     */
    @Test
    void testDeleteTaskGeofenceSummary() {
        TaskEntity task = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        String geofenceName = generateUniqueGeofenceName();
        String coordinates = generateCoordinates();

        TaskGeofenceSummaryEntity summary = new TaskGeofenceSummaryEntity(null, task.getId(), 10l, geofenceName, "RESTRICTED_AREA", coordinates, false, false);
        TaskGeofenceSummaryEntity savedSummary = taskGeofenceSummaryRepository.save(summary);

        taskGeofenceSummaryRepository.deleteById(savedSummary.getId());
        Optional<TaskGeofenceSummaryEntity> deletedSummary = taskGeofenceSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    /**
     * Test find by task id.
     */
    @Test
    void testFindByTaskId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task1.getId(), 10l, generateUniqueGeofenceName(), "DELIVERY_ZONE", generateCoordinates(), true, false));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task1.getId(), 20l, generateUniqueGeofenceName(), "PICKUP_ZONE", generateCoordinates(), false, true));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task2.getId(), 30l, generateUniqueGeofenceName(), "SERVICE_AREA", generateCoordinates(), true, true));

        List<TaskGeofenceSummaryEntity> summaries = taskGeofenceSummaryRepository.findByTaskId(task1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getTaskId().equals(task1.getId()))).isTrue();
    }

    /**
     * Test find by geofence id.
     */
    @Test
    void testFindByGeofenceId() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task1.getId(), 10l, generateUniqueGeofenceName(), "DELIVERY_ZONE", generateCoordinates(), true, false));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task2.getId(), 10l, generateUniqueGeofenceName(), "PICKUP_ZONE", generateCoordinates(), false, true));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task3.getId(), 30l, generateUniqueGeofenceName(), "SERVICE_AREA", generateCoordinates(), true, true));

        List<TaskGeofenceSummaryEntity> summaries = taskGeofenceSummaryRepository.findByGeofenceId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getGeofenceId().equals(10l))).isTrue();
    }

    /**
     * Test find by geofence type.
     */
    @Test
    void testFindByGeofenceType() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task1.getId(), 10l, generateUniqueGeofenceName(), "DELIVERY_ZONE", generateCoordinates(), true, false));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task2.getId(), 20l, generateUniqueGeofenceName(), "DELIVERY_ZONE", generateCoordinates(), false, true));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task3.getId(), 30l, generateUniqueGeofenceName(), "PICKUP_ZONE", generateCoordinates(), true, true));

        List<TaskGeofenceSummaryEntity> deliveryZones = taskGeofenceSummaryRepository.findByGeofenceType("DELIVERY_ZONE");
        assertThat(deliveryZones).hasSize(2);
        assertThat(deliveryZones.stream().allMatch(s -> s.getGeofenceType().equals("DELIVERY_ZONE"))).isTrue();
    }

    /**
     * Test find by is source.
     */
    @Test
    void testFindByIsSource() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task1.getId(), 10l, generateUniqueGeofenceName(), "DELIVERY_ZONE", generateCoordinates(), true, false));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task2.getId(), 20l, generateUniqueGeofenceName(), "PICKUP_ZONE", generateCoordinates(), true, true));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task3.getId(), 30l, generateUniqueGeofenceName(), "SERVICE_AREA", generateCoordinates(), false, true));

        List<TaskGeofenceSummaryEntity> sourceGeofences = taskGeofenceSummaryRepository.findByIsSource(true);
        assertThat(sourceGeofences).hasSize(2);
        assertThat(sourceGeofences.stream().allMatch(TaskGeofenceSummaryEntity::isSource)).isTrue();
    }

    /**
     * Test find by is destination.
     */
    @Test
    void testFindByIsDestination() {
        TaskEntity task1 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task2 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        TaskEntity task3 = taskRepository.save(new TaskEntity(null, "Task M", "Desc M", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 10l, 10l, "Addr M", null, "Dest M", null, "Comment M", 10l));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task1.getId(), 10l, generateUniqueGeofenceName(), "DELIVERY_ZONE", generateCoordinates(), true, true));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task2.getId(), 20l, generateUniqueGeofenceName(), "PICKUP_ZONE", generateCoordinates(), false, true));
        taskGeofenceSummaryRepository.save(new TaskGeofenceSummaryEntity(null, task3.getId(), 30l, generateUniqueGeofenceName(), "SERVICE_AREA", generateCoordinates(), true, false));

        List<TaskGeofenceSummaryEntity> destinationGeofences = taskGeofenceSummaryRepository.findByIsDestination(true);
        assertThat(destinationGeofences).hasSize(2);
        assertThat(destinationGeofences.stream().allMatch(TaskGeofenceSummaryEntity::isDestination)).isTrue();
    }
}
