/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.service;

import com.dailyminutes.laundry.task.domain.event.TaskCreatedEvent;
import com.dailyminutes.laundry.task.domain.event.*;
import com.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.dailyminutes.laundry.task.dto.CreateTaskRequest;
import com.dailyminutes.laundry.task.dto.TaskResponse;
import com.dailyminutes.laundry.task.dto.UpdateTaskRequest;
import com.dailyminutes.laundry.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Task service.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher events;

    /**
     * Create task task response.
     *
     * @param request the request
     * @return the task response
     */
    public TaskResponse createTask(CreateTaskRequest request) {
        TaskEntity task = new TaskEntity(null, request.name(), request.description(), request.type(), request.taskStartTime(), null, null, request.status(), request.teamId(), request.agentId(), request.sourceAddress(), request.sourceGeofenceId(), request.destinationAddress(), request.destinationGeofenceId(), request.taskComment(), request.orderId());
        TaskEntity savedTask = taskRepository.save(task);
        events.publishEvent(new TaskCreatedEvent(
                savedTask.getId(),
                savedTask.getOrderId(),
                savedTask.getName(),
                savedTask.getType().name(),
                savedTask.getStatus().name(),
                savedTask.getTaskStartTime(),
                savedTask.getSourceAddress(),
                savedTask.getSourceGeofenceId(),
                savedTask.getDestinationAddress(),
                savedTask.getDestinationGeofenceId(),
                savedTask.getAgentId(),
                savedTask.getTeamId()
        ));
        return toTaskResponse(savedTask);
    }

    /**
     * Update task task response.
     *
     * @param request the request
     * @return the task response
     */
    public TaskResponse updateTask(UpdateTaskRequest request) {
        TaskEntity existingTask = taskRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + request.id() + " not found."));

        var oldStatus = existingTask.getStatus();
        var oldAgentId = existingTask.getAgentId();

        existingTask.setName(request.name());
        existingTask.setDescription(request.description());
        existingTask.setType(request.type());
        existingTask.setTaskStartTime(request.taskStartTime());
        existingTask.setTaskUpdatedTime(request.taskUpdatedTime());
        existingTask.setTaskCompletedTime(request.taskCompletedTime());
        existingTask.setStatus(request.status());
        existingTask.setTeamId(request.teamId());
        existingTask.setAgentId(request.agentId());
        existingTask.setSourceAddress(request.sourceAddress());
        existingTask.setSourceGeofenceId(request.sourceGeofenceId());
        existingTask.setDestinationAddress(request.destinationAddress());
        existingTask.setDestinationGeofenceId(request.destinationGeofenceId());
        existingTask.setTaskComment(request.taskComment());
        existingTask.setOrderId(request.orderId());

        TaskEntity updatedTask = taskRepository.save(existingTask);

        events.publishEvent(new TaskUpdatedEvent(updatedTask.getId()));
        if (oldStatus != updatedTask.getStatus()) {
            events.publishEvent(new TaskStatusChangedEvent(updatedTask.getId(), updatedTask.getOrderId(), oldStatus.name(), updatedTask.getStatus().name()));
        }
        if (oldAgentId != updatedTask.getAgentId()) {
            events.publishEvent(new TaskAssignedToAgentEvent(updatedTask.getId(), updatedTask.getAgentId()));
        }

        return toTaskResponse(updatedTask);
    }

    /**
     * Delete task.
     *
     * @param id the id
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task with ID " + id + " not found.");
        }
        taskRepository.deleteById(id);
        events.publishEvent(new TaskDeletedEvent(id));
    }

    private TaskResponse toTaskResponse(TaskEntity entity) {
        return new TaskResponse(entity.getId(), entity.getName(), entity.getDescription(), entity.getType(), entity.getTaskStartTime(), entity.getTaskUpdatedTime(), entity.getTaskCompletedTime(), entity.getStatus(), entity.getTeamId(), entity.getAgentId(), entity.getSourceAddress(), entity.getSourceGeofenceId(), entity.getDestinationAddress(), entity.getDestinationGeofenceId(), entity.getTaskComment(), entity.getOrderId());
    }
}
