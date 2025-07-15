package com.dailyminutes.laundry.geofence.repository;


import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceStoreSummaryEntity;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.repository.StoreRepository;
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
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.geofence.repository",
        "com.dailyminutes.laundry.store.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.geofence.domain.model",
        "com.dailyminutes.laundry.store.domain.model"})
class GeofenceStoreSummaryRepositoryTest {

    @Autowired
    private GeofenceStoreSummaryRepository geofenceStoreSummaryRepository;

    @Autowired
    private GeofenceRepository geofenceRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void testSaveAndFindGeofenceStoreSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", null));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));


        GeofenceStoreSummaryEntity summary = new GeofenceStoreSummaryEntity(
                null, store.getId(), geofence.getId(), "Main Store", "123 Main St, City, State");
        GeofenceStoreSummaryEntity savedSummary = geofenceStoreSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getStoreId()).isEqualTo(store.getId());
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
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));

        GeofenceStoreSummaryEntity summary = new GeofenceStoreSummaryEntity(
                null, store.getId(), geofence.getId(), "Branch Store A", "456 Oak Ave, Town, State");
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
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));

        GeofenceStoreSummaryEntity summary = new GeofenceStoreSummaryEntity(
                null, store.getId(), geofence.getId(), "Store to Delete", "Address to Delete");
        GeofenceStoreSummaryEntity savedSummary = geofenceStoreSummaryRepository.save(summary);

        geofenceStoreSummaryRepository.deleteById(savedSummary.getId());
        Optional<GeofenceStoreSummaryEntity> deletedSummary = geofenceStoreSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByStoreId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", null));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", null));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));

        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store1.getId(), geofence1.getId(), "Store X", "Addr X"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store2.getId(), geofence2.getId(), "Store Y", "Addr Y"));

        Optional<GeofenceStoreSummaryEntity> foundSummary = geofenceStoreSummaryRepository.findByStoreId(store1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreName()).isEqualTo("Store X");
        assertThat(foundSummary.get().getGeofenceId()).isEqualTo(geofence1.getId());
    }

    @Test
    void testFindByGeofenceId() { // Test for finding by geofence ID
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", null));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", null));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));

        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store1.getId(), geofence1.getId(), "Store G1", "Addr G1"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store2.getId(), geofence1.getId(), "Store G2", "Addr G2"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store1.getId(), geofence2.getId(), "Store G3", "Addr G3"));

        List<GeofenceStoreSummaryEntity> storesInGeofence30 = geofenceStoreSummaryRepository.findByGeofenceId(geofence1.getId());
        assertThat(storesInGeofence30).hasSize(2);
        assertThat(storesInGeofence30.stream().allMatch(s -> s.getGeofenceId().equals(geofence1.getId()))).isTrue();
        assertThat(storesInGeofence30.stream().map(GeofenceStoreSummaryEntity::getStoreName))
                .containsExactlyInAnyOrder("Store G1", "Store G2");
    }


    @Test
    void testFindByStoreName() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", null));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store1.getId(), geofence1.getId(), "Unique Store Name", "Unique Address"));
        Optional<GeofenceStoreSummaryEntity> foundSummary = geofenceStoreSummaryRepository.findByStoreName("Unique Store Name");
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreId()).isEqualTo(store1.getId());
        assertThat(foundSummary.get().getGeofenceId()).isEqualTo(geofence1.getId());
    }

    @Test
    void testFindByStoreAddressContaining() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St, Central City", "123-456-7890", "test@example.com", null));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St, Central City", "123-456-7890", "test@example.com", null));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store3", "54 High St, North Avenue", "123-456-7890", "test@example.com", null));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        GeofenceEntity geofence3 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((5 5, 6 5, 6 6, 5 6, 5 5))", "TEST_ZONE", "Zone C to Delete", true));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store1.getId(), geofence1.getId(), "Store Alpha", "123 Main St, Central City"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store2.getId(), geofence2.getId(), "Store Beta", "200 Side Rd, Central City"));
        geofenceStoreSummaryRepository.save(new GeofenceStoreSummaryEntity(null, store3.getId(), geofence3.getId(), "Store Gamma", "300 Park Ave, West Town"));

        List<GeofenceStoreSummaryEntity> storesInCentralCity = geofenceStoreSummaryRepository.findByStoreAddressContaining("Central City");
        assertThat(storesInCentralCity).hasSize(2);
        assertThat(storesInCentralCity.stream().map(GeofenceStoreSummaryEntity::getStoreName))
                .containsExactlyInAnyOrder("Store Alpha", "Store Beta");
    }
}
