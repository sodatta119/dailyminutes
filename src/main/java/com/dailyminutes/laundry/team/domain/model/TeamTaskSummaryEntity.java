/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.team.domain.model; // Updated package name

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Read model for Task summary information relevant to the Team module.
 * This entity is populated via events from the Task module.
 */
@Table("DL_TEAM_TASK_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamTaskSummaryEntity {

    @Id
    private Long id; // Auto-generated primary key for this summary record

    private Long teamId; // The Team ID from the Team module

    private Long taskId; // The Task ID from the Task module (unique for this summary per task)

    private String taskType;

    private String taskStatus;

    private LocalDateTime taskStartTime;

    private Long agentId;

    private String agentName;

    private Long orderId;
}
