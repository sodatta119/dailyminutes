// FILE: src/main/java/com/dailyminutes/laundry/task/api/TaskController.java
package com.dailyminutes.laundry.task.api;

import com.dailyminutes.laundry.task.dto.*;
import com.dailyminutes.laundry.task.service.TaskQueryService;
import com.dailyminutes.laundry.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * The type Task controller.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskQueryService taskQueryService;

    /**
     * Create task response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.CREATED);
    }

    /**
     * Update task response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @Operation(summary = "Update an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(taskService.updateTask(request));
    }

    /**
     * Delete task response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Operation(summary = "Delete a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Gets task by id.
     *
     * @param id the id
     * @return the task by id
     */
    @Operation(summary = "Get a task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return taskQueryService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    /**
     * Gets all tasks.
     *
     * @return the all tasks
     */
    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "List of all tasks")
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskQueryService.findAllTasks());
    }

    /**
     * Gets geofence summary.
     *
     * @param id the id
     * @return the geofence summary
     */
    @Operation(summary = "Get geofence summary for a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found geofence summary"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}/geofence-summary")
    public ResponseEntity<List<TaskGeofenceSummaryResponse>> getGeofenceSummary(@PathVariable Long id) {
        return ResponseEntity.ok(taskQueryService.findGeofenceSummaryByTaskId(id));
    }

    /**
     * Gets team summary.
     *
     * @param id the id
     * @return the team summary
     */
    @Operation(summary = "Get team summary for a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found team summary"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}/team-summary")
    public ResponseEntity<List<TaskTeamSummaryResponse>> getTeamSummary(@PathVariable Long id) {
        return ResponseEntity.ok(taskQueryService.findTeamSummaryByTaskId(id));
    }
}
