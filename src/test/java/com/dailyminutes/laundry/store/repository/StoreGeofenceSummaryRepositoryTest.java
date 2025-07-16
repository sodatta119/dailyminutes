package com.dailyminutes.laundry.store.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.domain.model.StoreGeofenceSummaryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 12/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.store.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.store.domain.model") // Updated package name
class StoreGeofenceSummaryRepositoryTest {

    @Autowired
    private StoreGeofenceSummaryRepository storeGeofenceSummaryRepository;

    @Autowired
    private StoreRepository storeRepository;


    private String generateUniqueGeofenceName() {
        return "Geofence-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    void testSaveAndFindStoreGeofenceSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        String geofenceName = generateUniqueGeofenceName();

        StoreGeofenceSummaryEntity summary = new StoreGeofenceSummaryEntity(null, store.getId(), 10l, geofenceName, "DELIVERY_ZONE", true);
        StoreGeofenceSummaryEntity savedSummary = storeGeofenceSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getStoreId()).isEqualTo(store.getId());
        assertThat(savedSummary.getGeofenceId()).isEqualTo(10l);
        assertThat(savedSummary.getGeofenceName()).isEqualTo(geofenceName);
        assertThat(savedSummary.getGeofenceType()).isEqualTo("DELIVERY_ZONE");
        assertThat(savedSummary.isActive()).isTrue();

        Optional<StoreGeofenceSummaryEntity> foundSummary = storeGeofenceSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getGeofenceName()).isEqualTo(geofenceName);
    }

    @Test
    void testUpdateStoreGeofenceSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        String geofenceName = generateUniqueGeofenceName();

        StoreGeofenceSummaryEntity summary = new StoreGeofenceSummaryEntity(null, store.getId(), 10l, geofenceName, "PICKUP_ZONE", true);
        StoreGeofenceSummaryEntity savedSummary = storeGeofenceSummaryRepository.save(summary);

        savedSummary.setGeofenceType("SERVICE_AREA");
        savedSummary.setActive(false);
        storeGeofenceSummaryRepository.save(savedSummary);

        Optional<StoreGeofenceSummaryEntity> updatedSummary = storeGeofenceSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getGeofenceType()).isEqualTo("SERVICE_AREA");
        assertThat(updatedSummary.get().isActive()).isFalse();
    }

    @Test
    void testDeleteStoreGeofenceSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        String geofenceName = generateUniqueGeofenceName();

        StoreGeofenceSummaryEntity summary = new StoreGeofenceSummaryEntity(null, store.getId(), 10l, geofenceName, "RESTRICTED_AREA", false);
        StoreGeofenceSummaryEntity savedSummary = storeGeofenceSummaryRepository.save(summary);

        storeGeofenceSummaryRepository.deleteById(savedSummary.getId());
        Optional<StoreGeofenceSummaryEntity> deletedSummary = storeGeofenceSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByStoreId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store1.getId(), 10l, generateUniqueGeofenceName(), "DELIVERY_ZONE", true));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store1.getId(), 20l, generateUniqueGeofenceName(), "PICKUP_ZONE", true));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store2.getId(), 30l, generateUniqueGeofenceName(), "SERVICE_AREA", false));

        List<StoreGeofenceSummaryEntity> summaries = storeGeofenceSummaryRepository.findByStoreId(store1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getStoreId().equals(store1.getId()))).isTrue();
    }

    @Test
    void testFindByGeofenceId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store1.getId(), 10l, generateUniqueGeofenceName(), "DELIVERY_ZONE", true));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store2.getId(), 20l, generateUniqueGeofenceName(), "PICKUP_ZONE", false));

        Optional<StoreGeofenceSummaryEntity> foundSummary = storeGeofenceSummaryRepository.findByGeofenceId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getGeofenceType()).isEqualTo("DELIVERY_ZONE");
    }

    @Test
    void testFindByGeofenceType() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store1.getId(), 10l, generateUniqueGeofenceName(), "DELIVERY_ZONE", true));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store2.getId(), 20l, generateUniqueGeofenceName(), "DELIVERY_ZONE", false));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store3.getId(), 30l, generateUniqueGeofenceName(), "PICKUP_ZONE", true));

        List<StoreGeofenceSummaryEntity> deliveryZones = storeGeofenceSummaryRepository.findByGeofenceType("DELIVERY_ZONE");
        assertThat(deliveryZones).hasSize(2);
        assertThat(deliveryZones.stream().allMatch(s -> s.getGeofenceType().equals("DELIVERY_ZONE"))).isTrue();
    }

    @Test
    void testFindByActive() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store1.getId(), 10l, generateUniqueGeofenceName(), "SERVICE_AREA", true));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store2.getId(), 20l, generateUniqueGeofenceName(), "OTHER", true));
        storeGeofenceSummaryRepository.save(new StoreGeofenceSummaryEntity(null, store3.getId(), 30l, generateUniqueGeofenceName(), "RESTRICTED_AREA", false));

        List<StoreGeofenceSummaryEntity> activeSummaries = storeGeofenceSummaryRepository.findByActive(true);
        assertThat(activeSummaries).hasSize(2);
        assertThat(activeSummaries.stream().allMatch(StoreGeofenceSummaryEntity::isActive)).isTrue();
    }
}
