/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.task.repository; // Updated package name

import com.dailyminutes.laundry.task.domain.model.TaskGeofenceSummaryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * The interface Task geofence summary repository.
 */
public interface TaskGeofenceSummaryRepository extends ListCrudRepository<TaskGeofenceSummaryEntity, Long> {
    /**
     * Find by task id list.
     *
     * @param taskId the task id
     * @return the list
     */
    List<TaskGeofenceSummaryEntity> findByTaskId(Long taskId); // Useful for updates from Task events

    /**
     * Find by geofence id list.
     *
     * @param geofenceId the geofence id
     * @return the list
     */
    List<TaskGeofenceSummaryEntity> findByGeofenceId(Long geofenceId); // Useful for updates from Geofence events

    /**
     * Find by geofence type list.
     *
     * @param geofenceType the geofence type
     * @return the list
     */
    List<TaskGeofenceSummaryEntity> findByGeofenceType(String geofenceType);

    /**
     * Find by is source list.
     *
     * @param isSource the is source
     * @return the list
     */
    List<TaskGeofenceSummaryEntity> findByIsSource(boolean isSource);

    /**
     * Find by is destination list.
     *
     * @param isDestination the is destination
     * @return the list
     */
    List<TaskGeofenceSummaryEntity> findByIsDestination(boolean isDestination);
}
