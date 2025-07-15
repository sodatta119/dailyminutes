/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.order.domain.model; // Updated package name

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Read model for Task summary information relevant to the Order module.
 * This entity is populated via events from the Task module.
 */
@Table("DL_ORDER_TASK_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderTaskSummaryEntity {

    @Id
    private Long id;

    private Long orderId;

    private Long taskId;

    private String taskType;

    private String taskStatus;

    private LocalDateTime taskStartTime;

    private Long agentId;

    private String agentName;
}
