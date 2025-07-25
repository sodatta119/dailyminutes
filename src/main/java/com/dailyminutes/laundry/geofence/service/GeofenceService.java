/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.geofence.service;


import com.dailyminutes.laundry.geofence.domain.event.GeofenceCreatedEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceDeletedEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceUpdatedEvent;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.dto.CreateGeofenceRequest;
import com.dailyminutes.laundry.geofence.dto.GeofenceResponse;
import com.dailyminutes.laundry.geofence.dto.UpdateGeofenceRequest;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GeofenceService {

    private final GeofenceRepository geofenceRepository;
    private final ApplicationEventPublisher events;

    public GeofenceResponse createGeofence(CreateGeofenceRequest request) {
        GeofenceEntity geofence = new GeofenceEntity(null, request.polygonCoordinates(), request.geofenceType(), request.name(), request.active());
        GeofenceEntity savedGeofence = geofenceRepository.save(geofence);
        events.publishEvent(new GeofenceCreatedEvent(savedGeofence.getId(), savedGeofence.getName(), savedGeofence.getGeofenceType(), savedGeofence.isActive()));
        return toGeofenceResponse(savedGeofence);
    }

    public GeofenceResponse updateGeofence(UpdateGeofenceRequest request) {
        GeofenceEntity existingGeofence = geofenceRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Geofence with ID " + request.id() + " not found."));

        existingGeofence.setPolygonCoordinates(request.polygonCoordinates());
        existingGeofence.setGeofenceType(request.geofenceType());
        existingGeofence.setName(request.name());
        existingGeofence.setActive(request.active());

        GeofenceEntity updatedGeofence = geofenceRepository.save(existingGeofence);
        events.publishEvent(new GeofenceUpdatedEvent(updatedGeofence.getId(), updatedGeofence.getName(), updatedGeofence.getGeofenceType(), updatedGeofence.isActive()));
        return toGeofenceResponse(updatedGeofence);
    }

    public void deleteGeofence(Long id) {
        if (!geofenceRepository.existsById(id)) {
            throw new IllegalArgumentException("Geofence with ID " + id + " not found.");
        }
        geofenceRepository.deleteById(id);
        events.publishEvent(new GeofenceDeletedEvent(id));
    }

    private GeofenceResponse toGeofenceResponse(GeofenceEntity entity) {
        return new GeofenceResponse(entity.getId(), entity.getPolygonCoordinates(), entity.getGeofenceType(), entity.getName(), entity.isActive());
    }
}
