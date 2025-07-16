package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceOrderSummaryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Geofence order summary repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.geofence.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.geofence.domain.model"})
class GeofenceOrderSummaryRepositoryTest {

    @Autowired
    private GeofenceOrderSummaryRepository geofenceOrderSummaryRepository;


    @Autowired
    private GeofenceRepository geofenceRepository;


    /**
     * Test save and find geofence order summary.
     */
    @Test
    void testSaveAndFindGeofenceOrderSummary() {
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceOrderSummaryEntity summary = new GeofenceOrderSummaryEntity(
                null, 10l, geofence.getId(), LocalDateTime.now(), "PENDING", new BigDecimal("50.00"), 20l, 30l);
        GeofenceOrderSummaryEntity savedSummary = geofenceOrderSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getOrderId()).isEqualTo(10l);
        assertThat(savedSummary.getGeofenceId()).isEqualTo(geofence.getId());
        assertThat(savedSummary.getCustomerId()).isEqualTo(20l);
        assertThat(savedSummary.getStoreId()).isEqualTo(30l);

        Optional<GeofenceOrderSummaryEntity> foundSummary = geofenceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("PENDING");
        assertThat(foundSummary.get().getTotalAmount()).isEqualByComparingTo("50.00");
    }

    /**
     * Test update geofence order summary.
     */
    @Test
    void testUpdateGeofenceOrderSummary() {
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceOrderSummaryEntity summary = new GeofenceOrderSummaryEntity(
                null, 10l, geofence.getId(), LocalDateTime.now(), "ACCEPTED", new BigDecimal("75.00"), 20l, 30l);
        GeofenceOrderSummaryEntity savedSummary = geofenceOrderSummaryRepository.save(summary);

        savedSummary.setStatus("DELIVERED");
        savedSummary.setTotalAmount(new BigDecimal("70.00"));
        geofenceOrderSummaryRepository.save(savedSummary);

        Optional<GeofenceOrderSummaryEntity> updatedSummary = geofenceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStatus()).isEqualTo("DELIVERED");
        assertThat(updatedSummary.get().getTotalAmount()).isEqualByComparingTo("70.00");
    }

    /**
     * Test delete geofence order summary.
     */
    @Test
    void testDeleteGeofenceOrderSummary() {
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceOrderSummaryEntity summary = new GeofenceOrderSummaryEntity(
                null, 10l, geofence.getId(), LocalDateTime.now(), "CANCELLED", new BigDecimal("20.00"), 20l, 30l);
        GeofenceOrderSummaryEntity savedSummary = geofenceOrderSummaryRepository.save(summary);

        geofenceOrderSummaryRepository.deleteById(savedSummary.getId());
        Optional<GeofenceOrderSummaryEntity> deletedSummary = geofenceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    /**
     * Test find by geofence id.
     */
    @Test
    void testFindByGeofenceId() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 10l, geofence1.getId(), LocalDateTime.now(), "PENDING", new BigDecimal("10.00"), 10l, 10l));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 20l, geofence1.getId(), LocalDateTime.now(), "DELIVERED", new BigDecimal("25.00"), 20l, 20l));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 30l, geofence2.getId(), LocalDateTime.now(), "NEW", new BigDecimal("30.00"), 30l, 30l));

        List<GeofenceOrderSummaryEntity> ordersForGeofence10 = geofenceOrderSummaryRepository.findByGeofenceId(geofence1.getId());
        assertThat(ordersForGeofence10).hasSize(2);
        assertThat(ordersForGeofence10.stream().allMatch(s -> s.getGeofenceId().equals(geofence1.getId()))).isTrue();
    }

    /**
     * Test find by order id.
     */
    @Test
    void testFindByOrderId() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 10l, geofence1.getId(), LocalDateTime.now(), "NEW", new BigDecimal("15.00"), 10l, 10l));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 20l, geofence2.getId(), LocalDateTime.now(), "ACCEPTED", new BigDecimal("18.00"), 20l, 20l));

        Optional<GeofenceOrderSummaryEntity> foundSummary = geofenceOrderSummaryRepository.findByOrderId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getGeofenceId()).isEqualTo(geofence1.getId());
    }

    /**
     * Test find by customer id.
     */
    @Test
    void testFindByCustomerId() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 10l, geofence1.getId(), LocalDateTime.now(), "PENDING", new BigDecimal("100.00"), 10l, 10l));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 20l, geofence1.getId(), LocalDateTime.now(), "DELIVERED", new BigDecimal("120.00"), 10l, 20l));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 30l, geofence2.getId(), LocalDateTime.now(), "ACCEPTED", new BigDecimal("80.00"), 10l, 30l));

        List<GeofenceOrderSummaryEntity> ordersForCustomer40 = geofenceOrderSummaryRepository.findByCustomerId(10l);
        assertThat(ordersForCustomer40).hasSize(3);
        assertThat(ordersForCustomer40.stream().allMatch(s -> s.getCustomerId().equals(10l))).isTrue();
    }

    /**
     * Test find by store id.
     */
    @Test
    void testFindByStoreId() {
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 10l, geofence1.getId(), LocalDateTime.now(), "PENDING", new BigDecimal("60.00"), 10l, 10l));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 20l, geofence1.getId(), LocalDateTime.now(), "DELIVERED", new BigDecimal("70.00"), 20l, 10l));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, 30l, geofence2.getId(), LocalDateTime.now(), "ACCEPTED", new BigDecimal("80.00"), 30l, 10l));

        List<GeofenceOrderSummaryEntity> ordersForStore500 = geofenceOrderSummaryRepository.findByStoreId(10l);
        assertThat(ordersForStore500).hasSize(3);
        assertThat(ordersForStore500.stream().allMatch(s -> s.getStoreId().equals(10l))).isTrue();
    }
}
