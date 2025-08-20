package com.dailyminutes.laundry.task.service;

import com.dailyminutes.laundry.task.domain.event.TaskCreatedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskDeletedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskUpdatedEvent;
import com.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskType;
import com.dailyminutes.laundry.task.dto.CreateTaskRequest;
import com.dailyminutes.laundry.task.dto.UpdateTaskRequest;
import com.dailyminutes.laundry.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The type Task service test.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private TaskService taskService;

    /**
     * Create task should create and publish event.
     */
    @Test
    void createTask_shouldCreateAndPublishEvent() {
        CreateTaskRequest request = new CreateTaskRequest("name", "desc", TaskType.PICKUP, LocalDateTime.now(), TaskStatus.NEW, 1L, 1L, "source", 1L, "dest", 1L, "comment", 1L);
        TaskEntity task = new TaskEntity(1L, "name", "desc", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 1L, 1L, "source", 1L, "dest", 1L, "comment", 1L);
        when(taskRepository.save(any())).thenReturn(task);

        taskService.createTask(request);

        verify(events).publishEvent(any(TaskCreatedEvent.class));
    }

    /**
     * Update task should update and publish event.
     */
    @Test
    void updateTask_shouldUpdateAndPublishEvent() {
        UpdateTaskRequest request = new UpdateTaskRequest(1L, "name", "desc", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, 1L, 1L, "source", 1L, "dest", 1L, "comment", 1L);
        TaskEntity task = new TaskEntity(1L, "name", "desc", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 1L, 1L, "source", 1L, "dest", 1L, "comment", 1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        taskService.updateTask(request);

        verify(events).publishEvent(any(TaskUpdatedEvent.class));
    }

    /**
     * Delete task should delete and publish event.
     */
    @Test
    void deleteTask_shouldDeleteAndPublishEvent() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(events).publishEvent(any(TaskDeletedEvent.class));
    }

    /**
     * Update task should throw exception when task not found.
     */
    @Test
    void updateTask_shouldThrowException_whenTaskNotFound() {
        UpdateTaskRequest request = new UpdateTaskRequest(1L, "name", "desc", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, 1L, 1L, "source", 1L, "dest", 1L, "comment", 1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(request));
    }
}
