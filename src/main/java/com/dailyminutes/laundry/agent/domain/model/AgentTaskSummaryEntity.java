/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.agent.domain.model;

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
 * Read model for Task summary information relevant to the Agent module.
 * This entity is populated via events from the Task module.
 */
@Table("DL_AGENT_TASK_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AgentTaskSummaryEntity {

    @Id
    private Long id;

    private Long taskId;

    private Long agentId;

    private String taskName;

    private String taskType; // Storing enum as String

    private String taskStatus; // Storing enum as String

    private LocalDateTime taskStartTime;

    private String sourceAddress;

    private String destinationAddress;

    private Long orderId;
}
