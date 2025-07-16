package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Geofence repository test.
 */
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.geofence.repository") // Specify repository package
@ComponentScan(basePackages = "com.dailyminutes.laundry.geofence.domain.model") // Specify domain model package
class GeofenceRepositoryTest {

    @Autowired
    private GeofenceRepository geofenceRepository;

    /**
     * Test save and find geofence.
     */
    @Test
    void testSaveAndFindGeofence() {
        GeofenceEntity geofence = new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true);
        GeofenceEntity savedGeofence = geofenceRepository.save(geofence);

        assertThat(savedGeofence).isNotNull();
        assertThat(savedGeofence.getId()).isNotNull();

        Optional<GeofenceEntity> foundGeofence = geofenceRepository.findById(savedGeofence.getId());
        assertThat(foundGeofence).isPresent();
        assertThat(foundGeofence.get().getName()).isEqualTo("Zone A");
    }

    /**
     * Test update geofence.
     */
    @Test
    void testUpdateGeofence() {
        GeofenceEntity geofence = new GeofenceEntity(null, "POLYGON((2 2, 3 2, 3 3, 2 3, 2 2))", "PICKUP_ZONE", "Zone B", true);
        GeofenceEntity savedGeofence = geofenceRepository.save(geofence);

        savedGeofence.setName("Updated Zone B");
        savedGeofence.setActive(false);
        GeofenceEntity updatedGeofence = geofenceRepository.save(savedGeofence);

        Optional<GeofenceEntity> foundUpdatedGeofence = geofenceRepository.findById(updatedGeofence.getId());
        assertThat(foundUpdatedGeofence).isPresent();
        assertThat(foundUpdatedGeofence.get().getName()).isEqualTo("Updated Zone B");
        assertThat(foundUpdatedGeofence.get().isActive()).isFalse();
    }

    /**
     * Test delete geofence.
     */
    @Test
    void testDeleteGeofence() {
        GeofenceEntity geofence = new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true);
        GeofenceEntity savedGeofence = geofenceRepository.save(geofence);

        geofenceRepository.deleteById(savedGeofence.getId());

        Optional<GeofenceEntity> deletedGeofence = geofenceRepository.findById(savedGeofence.getId());
        assertThat(deletedGeofence).isNotPresent();
    }
}
