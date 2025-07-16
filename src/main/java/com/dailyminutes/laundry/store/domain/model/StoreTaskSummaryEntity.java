/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.domain.model; // Updated package name

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
 * Read model for Task summary information relevant to the Store module.
 * This entity is populated via events from the Task module.
 */
@Table("DL_STORE_TASK_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreTaskSummaryEntity {

    @Id
    private Long id;

    private Long storeId;

    private Long taskId;

    private String taskType;

    private String taskStatus;

    private LocalDateTime taskStartTime;

    private Long agentId;

    private String agentName;

    private Long orderId;
}
