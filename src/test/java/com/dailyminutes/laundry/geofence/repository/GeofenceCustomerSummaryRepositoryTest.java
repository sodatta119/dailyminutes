package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceCustomerSummaryEntity;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
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

@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.geofence.repository",
        "com.dailyminutes.laundry.customer.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.geofence.domain.model",
        "com.dailyminutes.laundry.customer.domain.model"})
class GeofenceCustomerSummaryRepositoryTest {

    @Autowired
    private GeofenceCustomerSummaryRepository geofenceCustomerSummaryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GeofenceRepository geofenceRepository;

    @Test
    void testSaveAndFindGeofenceCustomerSummary() {
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceCustomerSummaryEntity summary = new GeofenceCustomerSummaryEntity(
                null, customer.getId(), geofence.getId(), "John Doe", "1234567890");
        GeofenceCustomerSummaryEntity savedSummary = geofenceCustomerSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getCustomerId()).isEqualTo(customer.getId());
        assertThat(savedSummary.getGeofenceId()).isEqualTo(geofence.getId());
        assertThat(savedSummary.getCustomerName()).isEqualTo("John Doe");
        assertThat(savedSummary.getCustomerPhoneNumber()).isEqualTo("1234567890");

        Optional<GeofenceCustomerSummaryEntity> foundSummary = geofenceCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void testUpdateGeofenceCustomerSummary() {
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceCustomerSummaryEntity summary = new GeofenceCustomerSummaryEntity(
                null, customer.getId(), geofence.getId(), "Jane Smith", "0987654321");
        GeofenceCustomerSummaryEntity savedSummary = geofenceCustomerSummaryRepository.save(summary);

        savedSummary.setCustomerName("Jane A. Smith");
        savedSummary.setCustomerPhoneNumber("1112223333");
        geofenceCustomerSummaryRepository.save(savedSummary);

        Optional<GeofenceCustomerSummaryEntity> updatedSummary = geofenceCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getCustomerName()).isEqualTo("Jane A. Smith");
        assertThat(updatedSummary.get().getCustomerPhoneNumber()).isEqualTo("1112223333");
    }

    @Test
    void testDeleteGeofenceCustomerSummary() {
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceCustomerSummaryEntity summary = new GeofenceCustomerSummaryEntity(
                null, customer.getId(), geofence.getId(), "Customer to Delete", "5555555555");
        GeofenceCustomerSummaryEntity savedSummary = geofenceCustomerSummaryRepository.save(summary);

        geofenceCustomerSummaryRepository.deleteById(savedSummary.getId());
        Optional<GeofenceCustomerSummaryEntity> deletedSummary = geofenceCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByGeofenceId() {
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB123", "4545343443", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB124", "5645634534", "Jane Doe", "jane@example.com"));
        CustomerEntity customer3 = customerRepository.save(new CustomerEntity(null, "SUB125", "8767678677", "Jane Doe", "jane@example.com"));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceCustomerSummaryRepository.save(new GeofenceCustomerSummaryEntity(null, customer1.getId(), geofence1.getId(), "Cust A", "111"));
        geofenceCustomerSummaryRepository.save(new GeofenceCustomerSummaryEntity(null, customer2.getId(), geofence1.getId(), "Cust B", "222"));
        geofenceCustomerSummaryRepository.save(new GeofenceCustomerSummaryEntity(null, customer3.getId(), geofence2.getId(), "Cust C", "333"));

        List<GeofenceCustomerSummaryEntity> customersInGeofence20 = geofenceCustomerSummaryRepository.findByGeofenceId(geofence1.getId());
        assertThat(customersInGeofence20).hasSize(2);
        assertThat(customersInGeofence20.stream().allMatch(s -> s.getGeofenceId().equals(geofence1.getId()))).isTrue();
        assertThat(customersInGeofence20.stream().map(GeofenceCustomerSummaryEntity::getCustomerName))
                .containsExactlyInAnyOrder("Cust A", "Cust B");
    }

    @Test
    void testFindByCustomerId() {
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB123", "4545343443", "Jane Doe", "jane@example.com"));
        CustomerEntity customer2 = customerRepository.save(new CustomerEntity(null, "SUB124", "5645634534", "Jane Doe", "jane@example.com"));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceCustomerSummaryRepository.save(new GeofenceCustomerSummaryEntity(null, customer1.getId(), geofence1.getId(), "Cust D", "444"));
        geofenceCustomerSummaryRepository.save(new GeofenceCustomerSummaryEntity(null, customer2.getId(), geofence2.getId(), "Cust E", "555"));

        Optional<GeofenceCustomerSummaryEntity> foundSummary = geofenceCustomerSummaryRepository.findByCustomerId(customer1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Cust D");
        assertThat(foundSummary.get().getGeofenceId()).isEqualTo(geofence1.getId());
    }

    @Test
    void testFindByCustomerPhoneNumber() {
        CustomerEntity customer1 = customerRepository.save(new CustomerEntity(null, "SUB123", "4545343443", "Jane Doe", "jane@example.com"));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceCustomerSummaryRepository.save(new GeofenceCustomerSummaryEntity(null, customer1.getId(), geofence1.getId(), "Cust F", "9998887777"));
        Optional<GeofenceCustomerSummaryEntity> foundSummary = geofenceCustomerSummaryRepository.findByCustomerPhoneNumber("9998887777");
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Cust F");
    }
}
