/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.laundry.task.domain.model.TaskGeofenceSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface TaskGeofenceSummaryRepository extends ListCrudRepository<TaskGeofenceSummaryEntity, Long> {
    List<TaskGeofenceSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events
    List<TaskGeofenceSummaryEntity> findByGeofenceId(Long geofenceId); // Useful for updates from Geofence events
    List<TaskGeofenceSummaryEntity> findByGeofenceType(String geofenceType);
    List<TaskGeofenceSummaryEntity> findByIsSource(boolean isSource);
    List<TaskGeofenceSummaryEntity> findByIsDestination(boolean isDestination);
}
