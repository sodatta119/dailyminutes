/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.task.domain.model; // Updated package name

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Read model for Team summary information relevant to the Task module.
 * This entity is populated via events from the Team module.
 */
@Table("DL_TASK_TEAM_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskTeamSummaryEntity {

    @Id
    private Long id; // Auto-generated primary key for this summary record

    private Long taskId; // The Task ID from the Task module (unique for this summary per task)

    private Long teamId; // The Team ID from the Team module

    private String teamName;

    private String teamDescription;

    private String teamRole;
}
