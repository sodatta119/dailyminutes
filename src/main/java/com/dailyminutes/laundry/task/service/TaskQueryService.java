/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.service;

import com.dailyminutes.laundry.task.dto.*;
import com.dailyminutes.laundry.task.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskQueryService {

    private final TaskRepository taskRepository;
    private final TaskAgentSummaryRepository agentSummaryRepository;
    private final TaskGeofenceSummaryRepository geofenceSummaryRepository;
    private final TaskOrderSummaryRepository orderSummaryRepository;
    private final TaskTeamSummaryRepository teamSummaryRepository;

    public Optional<TaskResponse> findTaskById(Long id) {
        return taskRepository.findById(id)
                .map(t -> new TaskResponse(t.getId(), t.getName(), t.getDescription(), t.getType(), t.getTaskStartTime(), t.getTaskUpdatedTime(), t.getTaskCompletedTime(), t.getStatus(), t.getTeamId(), t.getAgentId(), t.getSourceAddress(), t.getSourceGeofenceId(), t.getDestinationAddress(), t.getDestinationGeofenceId(), t.getTaskComment(), t.getOrderId()));
    }

    public List<TaskResponse> findAllTasks() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                .map(t -> new TaskResponse(t.getId(), t.getName(), t.getDescription(), t.getType(), t.getTaskStartTime(), t.getTaskUpdatedTime(), t.getTaskCompletedTime(), t.getStatus(), t.getTeamId(), t.getAgentId(), t.getSourceAddress(), t.getSourceGeofenceId(), t.getDestinationAddress(), t.getDestinationGeofenceId(), t.getTaskComment(), t.getOrderId()))
                .collect(Collectors.toList());
    }

    public List<TaskAgentSummaryResponse> findAgentSummaryByTaskId(Long taskId) {
        return agentSummaryRepository.findByTaskId(taskId).stream()
                .map(s -> new TaskAgentSummaryResponse(s.getId(), s.getTaskId(), s.getAgentId(), s.getAgentName(), s.getAgentPhoneNumber(), s.getAgentDesignation(), s.getAgentState()))
                .collect(Collectors.toList());
    }

    public List<TaskGeofenceSummaryResponse> findGeofenceSummaryByTaskId(Long taskId) {
        return geofenceSummaryRepository.findByTaskId(taskId).stream()
                .map(s -> new TaskGeofenceSummaryResponse(s.getId(), s.getTaskId(), s.getGeofenceId(), s.getGeofenceName(), s.getGeofenceType(), s.getPolygonCoordinates(), s.isSource(), s.isDestination()))
                .collect(Collectors.toList());
    }

    public List<TaskOrderSummaryResponse> findOrderSummaryByTaskId(Long taskId) {
        return orderSummaryRepository.findByTaskId(taskId).stream()
                .map(s -> new TaskOrderSummaryResponse(s.getId(), s.getTaskId(), s.getOrderId(), s.getOrderDate(), s.getStatus(), s.getTotalAmount(), s.getCustomerId(), s.getStoreId()))
                .collect(Collectors.toList());
    }

    public List<TaskTeamSummaryResponse> findTeamSummaryByTaskId(Long taskId) {
        return teamSummaryRepository.findByTaskId(taskId).stream()
                .map(s -> new TaskTeamSummaryResponse(s.getId(), s.getTaskId(), s.getTeamId(), s.getTeamName(), s.getTeamDescription(), s.getTeamRole()))
                .collect(Collectors.toList());
    }
}
