package com.dailyminutes.laundry.task.api;

import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskType;
import com.dailyminutes.laundry.task.dto.CreateTaskRequest;
import com.dailyminutes.laundry.task.dto.TaskResponse;
import com.dailyminutes.laundry.task.dto.UpdateTaskRequest;
import com.dailyminutes.laundry.task.service.TaskQueryService;
import com.dailyminutes.laundry.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Task controller test.
 */
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private TaskService taskService;
    @MockitoBean
    private TaskQueryService taskQueryService;

    private TaskResponse taskResponse;
    private CreateTaskRequest createTaskRequest;
    private UpdateTaskRequest updateTaskRequest;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        taskResponse = new TaskResponse(1L, "name", "desc", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 1L, 1L, "source", 1L, "dest", 1L, "comment", 1L);
        createTaskRequest = new CreateTaskRequest("name", "desc", TaskType.PICKUP, LocalDateTime.now(), TaskStatus.NEW, 1L, 1L, "source", 1L, "dest", 1L, "comment", 1L);
        updateTaskRequest = new UpdateTaskRequest(1L, "updated name", "updated desc", TaskType.DELIVERY, LocalDateTime.now(), null, null, TaskStatus.ASSIGNED, 2L, 2L, "new source", 2L, "new dest", 2L, "new comment", 2L);
    }

    /**
     * Create task should return created.
     *
     * @throws Exception the exception
     */
    @Test
    void createTask_shouldReturnCreated() throws Exception {
        when(taskService.createTask(any())).thenReturn(taskResponse);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskRequest)))
                .andExpect(status().isCreated());
    }

    /**
     * Gets task by id should return task.
     *
     * @throws Exception the exception
     */
    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        when(taskQueryService.findTaskById(1L)).thenReturn(Optional.of(taskResponse));
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"));
    }

    /**
     * Update task should return ok.
     *
     * @throws Exception the exception
     */
    @Test
    void updateTask_shouldReturnOk() throws Exception {
        when(taskService.updateTask(any())).thenReturn(taskResponse);
        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskRequest)))
                .andExpect(status().isOk());
    }

    /**
     * Delete task should return no content.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(1L);
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
