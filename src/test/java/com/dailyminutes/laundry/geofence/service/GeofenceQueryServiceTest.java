package com.dailyminutes.laundry.geofence.service;


import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * The type Geofence query service test.
 */
@ExtendWith(MockitoExtension.class)
class GeofenceQueryServiceTest {

    @Mock
    private GeofenceRepository geofenceRepository;
    @InjectMocks
    private GeofenceQueryService geofenceQueryService;

    /**
     * Find geofence by id should return geofence.
     */
    @Test
    void findGeofenceById_shouldReturnGeofence() {
        GeofenceEntity geofence = new GeofenceEntity(1L, "coords", "type", "name", true, "99L", LocalDateTime.now(), false);
        when(geofenceRepository.findById(1L)).thenReturn(Optional.of(geofence));

        assertThat(geofenceQueryService.findGeofenceById(1L)).isPresent();
    }
}
