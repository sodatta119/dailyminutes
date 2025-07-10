package com.chitchatfm.dailyminutes.laundry.geofence.repository;

import com.chitchatfm.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GeofenceRepositoryTest {

    @Autowired
    private GeofenceRepository geofenceRepository;

    @Test
    void testSaveAndFindGeofence() {
        GeofenceEntity geofence = new GeofenceEntity(null, 1L, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true);
        GeofenceEntity savedGeofence = geofenceRepository.save(geofence);

        assertThat(savedGeofence).isNotNull();
        assertThat(savedGeofence.getId()).isNotNull();

        Optional<GeofenceEntity> foundGeofence = geofenceRepository.findById(savedGeofence.getId());
        assertThat(foundGeofence).isPresent();
        assertThat(foundGeofence.get().getName()).isEqualTo("Zone A");
    }

    @Test
    void testFindByStoreId() {
        geofenceRepository.save(new GeofenceEntity(null, 100L, "Coords1", "TYPE1", "GF1", true));
        geofenceRepository.save(new GeofenceEntity(null, 100L, "Coords2", "TYPE2", "GF2", true));
        geofenceRepository.save(new GeofenceEntity(null, 200L, "Coords3", "TYPE3", "GF3", true));

        List<GeofenceEntity> geofences = geofenceRepository.findByStoreId(100L);
        assertThat(geofences).hasSize(2);
        assertThat(geofences.get(0).getStoreId()).isEqualTo(100L);
    }
}
