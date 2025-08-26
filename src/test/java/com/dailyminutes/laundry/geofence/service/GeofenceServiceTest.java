package com.dailyminutes.laundry.geofence.service;


import com.dailyminutes.laundry.geofence.domain.event.GeofenceCreatedEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceDeletedEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceUpdatedEvent;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.dto.CreateGeofenceRequest;
import com.dailyminutes.laundry.geofence.dto.UpdateGeofenceRequest;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The type Geofence service test.
 */
@ExtendWith(MockitoExtension.class)
class GeofenceServiceTest {

    @Mock
    private GeofenceRepository geofenceRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private GeofenceService geofenceService;

    /**
     * Create geofence should create and publish event.
     */
    @Test
    void createGeofence_shouldCreateAndPublishEvent() {
        CreateGeofenceRequest request = new CreateGeofenceRequest("coords", "type", "name", true);
        GeofenceEntity geofence = new GeofenceEntity(1L, "coords", "type", "name", true, "99L", LocalDateTime.now(), false);
        when(geofenceRepository.save(any())).thenReturn(geofence);

        geofenceService.createGeofence(request);

        verify(events).publishEvent(any(GeofenceCreatedEvent.class));
    }

    /**
     * Update geofence should update and publish event.
     */
    @Test
    void updateGeofence_shouldUpdateAndPublishEvent() {
        UpdateGeofenceRequest request = new UpdateGeofenceRequest(1L, "new-coords", "new-type", "new-name", false);
        GeofenceEntity geofence = new GeofenceEntity(1L, "coords", "type", "name", true, "99L", LocalDateTime.now(), false);
        when(geofenceRepository.findById(1L)).thenReturn(Optional.of(geofence));
        when(geofenceRepository.save(any())).thenReturn(geofence);

        geofenceService.updateGeofence(request);

        verify(events).publishEvent(any(GeofenceUpdatedEvent.class));
    }

    /**
     * Delete geofence should delete and publish event.
     */
    @Test
    void deleteGeofence_shouldDeleteAndPublishEvent() {
        when(geofenceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(geofenceRepository).deleteById(1L);

        geofenceService.deleteGeofence(1L);

        verify(events).publishEvent(any(GeofenceDeletedEvent.class));
    }

    /**
     * Update geofence should throw exception when geofence not found.
     */
    @Test
    void updateGeofence_shouldThrowException_whenGeofenceNotFound() {
        UpdateGeofenceRequest request = new UpdateGeofenceRequest(1L, "new-coords", "new-type", "new-name", false);
        when(geofenceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> geofenceService.updateGeofence(request));
    }
}
