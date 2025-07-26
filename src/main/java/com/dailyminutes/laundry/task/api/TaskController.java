/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.task.api;

import com.dailyminutes.laundry.task.dto.*;
import com.dailyminutes.laundry.task.service.TaskQueryService;
import com.dailyminutes.laundry.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskQueryService taskQueryService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(taskService.updateTask(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return taskQueryService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskQueryService.findAllTasks());
    }

    @GetMapping("/{id}/agent-summary")
    public ResponseEntity<List<TaskAgentSummaryResponse>> getAgentSummary(@PathVariable Long id) {
        return ResponseEntity.ok(taskQueryService.findAgentSummaryByTaskId(id));
    }

    @GetMapping("/{id}/geofence-summary")
    public ResponseEntity<List<TaskGeofenceSummaryResponse>> getGeofenceSummary(@PathVariable Long id) {
        return ResponseEntity.ok(taskQueryService.findGeofenceSummaryByTaskId(id));
    }

    @GetMapping("/{id}/order-summary")
    public ResponseEntity<List<TaskOrderSummaryResponse>> getOrderSummary(@PathVariable Long id) {
        return ResponseEntity.ok(taskQueryService.findOrderSummaryByTaskId(id));
    }

    @GetMapping("/{id}/team-summary")
    public ResponseEntity<List<TaskTeamSummaryResponse>> getTeamSummary(@PathVariable Long id) {
        return ResponseEntity.ok(taskQueryService.findTeamSummaryByTaskId(id));
    }
}
