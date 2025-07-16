package com.dailyminutes.laundry.geofence.repository;


import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceStoreSummaryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.geofence.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.geofence.domain.model"})
class GeofenceStoreSummaryRepositoryTest {

    @Autowired
    private GeofenceStoreSummaryRepository geofenceStoreSummaryRepository;

    @Autowired
    private GeofenceRepository geofenceRepository;

    @Test
    void testSaveAndFindGeofenceStoreSummary() {
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));


        GeofenceStoreSummaryEntity summary = new GeofenceStoreSummaryEntity(
                null, 10l, geofence.getId(), "Main Store", "123 Main St, City, State");
        GeofenceStoreSummaryEntity savedSummary = geofenceStoreSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getStoreId()).isEqualTo(10l);
        assertThat(savedSummary.getGeofenceId()).isEqualTo(geofence.getId()); // Assert geofenceId
        assertThat(savedSummary.getStoreName()).isEqualTo("Main Store");
        assertThat(savedSummary.getStoreAddress()).isEqualTo("123 Main St, City, State");

        Optional<GeofenceStoreSummaryEntity> foundSummary = geofenceStoreSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreName()).isEqualTo("Main Store");
        assertThat(foundSummary.get().getGeofenceId()).isEqualTo(geofence.getId()); // Re-assert geofenceId
    }

    @Test
    void testUpdateGeofenceStoreSummary() {
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));

        GeofenceStoreSummaryEntity summary = new GeofenceStoreSummaryEntity(
                null, 10l, geofence.getId(), "Branch Store A", "456 Oak Ave, Town, State");
        GeofenceStoreSummaryEntity savedSummary = geofenceStoreSummaryRepository.save(summary);

        savedSummary.setStoreAddress("789 Pine Ln, New City, State");
        savedSummary.setStoreName("Branch Store A - Updated");
        geofenceStoreSummaryRepository.save(savedSummary);

        Optional<GeofenceStoreSummaryEntity> updatedSummary = geofenceStoreSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStoreAddress()).isEqualTo("789 Pine Ln, New City, State");
        assertThat(updatedSummary.get().getStoreName()).isEqualTo("Branch Store A - Updated");
    }

    @Test
    void testDeleteGeofenceStoreSummary() {
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));

        GeofenceStoreSummaryEntity summary = new GeofenceStoreSummaryEntity(
                null, 10l, geofence.getId(), "Store to Delete", "Address to Delete");
        GeofenceStoreSummaryEntity savedSummary = geofenceStoreSummaryRepository.save(summary);

        geofenceStoreSummaryRepository.deleteById(savedSummary.getId());
        Optional<GeofenceStoreSummaryEntity> deletedSummary = geofenceStoreSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByStoreId() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));

        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 10l, geofence1.getId(), "Store X", "Addr X"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 20l, geofence2.getId(), "Store Y", "Addr Y"));

        Optional<GeofenceStoreSummaryEntity> foundSummary = geofenceStoreSummaryRepository.findByStoreId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreName()).isEqualTo("Store X");
        assertThat(foundSummary.get().getGeofenceId()).isEqualTo(geofence1.getId());
    }

    @Test
    void testFindByGeofenceId() { // Test for finding by geofence ID
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));

        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 10l, geofence1.getId(), "Store G1", "Addr G1"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 20l, geofence1.getId(), "Store G2", "Addr G2"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 10l, geofence2.getId(), "Store G3", "Addr G3"));

        List<GeofenceStoreSummaryEntity> storesInGeofence30 = geofenceStoreSummaryRepository.findByGeofenceId(geofence1.getId());
        assertThat(storesInGeofence30).hasSize(2);
        assertThat(storesInGeofence30.stream().allMatch(s -> s.getGeofenceId().equals(geofence1.getId()))).isTrue();
        assertThat(storesInGeofence30.stream().map(GeofenceStoreSummaryEntity::getStoreName))
                .containsExactlyInAnyOrder("Store G1", "Store G2");
    }


    @Test
    void testFindByStoreName() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 10l, geofence1.getId(), "Unique Store Name", "Unique Address"));
        Optional<GeofenceStoreSummaryEntity> foundSummary = geofenceStoreSummaryRepository.findByStoreName("Unique Store Name");
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreId()).isEqualTo(10l);
        assertThat(foundSummary.get().getGeofenceId()).isEqualTo(geofence1.getId());
    }

    @Test
    void testFindByStoreAddressContaining() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        GeofenceEntity geofence3 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 10l, geofence1.getId(), "Store Alpha", "123 Main St, Central City"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 20l, geofence2.getId(), "Store Beta", "200 Side Rd, Central City"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, 30l, geofence3.getId(), "Store Gamma", "300 Park Ave, West Town"));

        List<GeofenceStoreSummaryEntity> storesInCentralCity = geofenceStoreSummaryRepository.findByStoreAddressContaining("Central City");
        assertThat(storesInCentralCity).hasSize(2);
        assertThat(storesInCentralCity.stream().map(GeofenceStoreSummaryEntity::getStoreName))
                .containsExactlyInAnyOrder("Store Alpha", "Store Beta");
    }
}
