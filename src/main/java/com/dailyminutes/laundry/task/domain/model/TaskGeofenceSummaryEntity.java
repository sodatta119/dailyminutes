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
 * Read model for Geofence summary information relevant to the Task module.
 * This entity is populated via events from the Geofence module.
 */
@Table("DL_TASK_GEOFENCE_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskGeofenceSummaryEntity {

    @Id
    private Long id; // Auto-generated primary key for this summary record

    private Long taskId; // The Task ID from the Task module

    private Long geofenceId; // The Geofence ID from the Geofence module (unique for this summary per geofence)

    private String geofenceName;

    private String geofenceType;

    private String polygonCoordinates;

    private boolean isSource;

    private boolean isDestination;
}
