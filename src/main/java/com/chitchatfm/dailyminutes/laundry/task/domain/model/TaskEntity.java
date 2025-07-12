/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.task.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * The type Task entity.
 */
@Table(name = "DL_TASK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskEntity {

    @Id
    private Long id;

    private String name;

    private String description;

    private TaskType type;

    private LocalDateTime taskStartTime;
    private LocalDateTime taskUpdatedTime;
    private LocalDateTime taskCompletedTime;

    private TaskStatus status;

    private Long teamId;

    private Long agentId;

    private String sourceAddress;

    private Long sourceGeofenceId;

    private String destinationAddress;

    private Long destinationGeofenceId;

    private String taskComment;

    private Long orderId;
}

