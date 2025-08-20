/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 20/08/25
 */
package com.dailyminutes.laundry.task.listener;

import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoRequestEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceInfoResponseEvent;
import com.dailyminutes.laundry.task.domain.event.TaskCreatedEvent;
import com.dailyminutes.laundry.task.domain.event.TaskDeletedEvent;
import com.dailyminutes.laundry.task.domain.model.TaskGeofenceSummaryEntity;
import com.dailyminutes.laundry.task.repository.TaskGeofenceSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskGeofenceEventListener {

    private final TaskGeofenceSummaryRepository summaryRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onTaskCreated(TaskCreatedEvent event) {
        // Ask for source geofence details, if it exists
        if (event.sourceGeofenceId() != null) {
            events.publishEvent(new GeofenceInfoRequestEvent(event.sourceGeofenceId(), event));
        }
        // Ask for destination geofence details, if it exists
        if (event.destinationGeofenceId() != null) {
            events.publishEvent(new GeofenceInfoRequestEvent(event.destinationGeofenceId(), event));
        }
    }

    @ApplicationModuleListener
    public void onGeofenceInfoProvided(GeofenceInfoResponseEvent event) {
        if(event.originalEvent() instanceof TaskCreatedEvent)
        {
            TaskCreatedEvent taskEvent= (TaskCreatedEvent) event.originalEvent();
            TaskGeofenceSummaryEntity summary = new TaskGeofenceSummaryEntity(
                    null,
                    taskEvent.taskId(),
                    event.geofenceId(),
                    event.geofenceName(),
                    event.geofenceType(),
                    event.polygonCoordinates(),
                    taskEvent.sourceGeofenceId()==event.geofenceId(),
                    taskEvent.destinationGeofenceId()==event.geofenceId()
            );
            summaryRepository.save(summary);
        }
    }

    @ApplicationModuleListener
    public void onTaskDeleted(TaskDeletedEvent event) {
        var summariesToDelete = summaryRepository.findByTaskId(event.taskId());
        summaryRepository.deleteAll(summariesToDelete);
    }
}