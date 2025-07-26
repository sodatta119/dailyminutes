package com.dailyminutes.laundry.task.service;

import com.dailyminutes.laundry.task.domain.model.TaskEntity;
import com.dailyminutes.laundry.task.domain.model.TaskStatus;
import com.dailyminutes.laundry.task.domain.model.TaskType;
import com.dailyminutes.laundry.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskQueryServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskQueryService taskQueryService;

    @Test
    void findTaskById_shouldReturnTask() {
        TaskEntity task = new TaskEntity(1L, "name", "desc", TaskType.PICKUP, LocalDateTime.now(), null, null, TaskStatus.NEW, 1L, 1L, "source", 1L, "dest", 1L, "comment", 1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThat(taskQueryService.findTaskById(1L)).isPresent();
    }
}
