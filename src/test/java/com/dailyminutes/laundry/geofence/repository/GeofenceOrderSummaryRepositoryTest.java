package com.dailyminutes.laundry.geofence.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceEntity;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceOrderSummaryEntity;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.dailyminutes.laundry.order.repository.OrderRepository;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.repository.StoreRepository;
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
        "com.dailyminutes.laundry.order.repository",
        "com.dailyminutes.laundry.customer.repository",
        "com.dailyminutes.laundry.store.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.geofence.domain.model",
        "com.dailyminutes.laundry.order.domain.model",
        "com.dailyminutes.laundry.customer.domain.model",
        "com.dailyminutes.laundry.store.domain.model"})
class GeofenceOrderSummaryRepositoryTest {

    @Autowired
    private GeofenceOrderSummaryRepository geofenceOrderSummaryRepository;


    @Autowired
    private GeofenceRepository geofenceRepository;


    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private StoreRepository storeRepository;

    @Test
    void testSaveAndFindGeofenceOrderSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        OrderEntity order = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceOrderSummaryEntity summary = new GeofenceOrderSummaryEntity(
                null, order.getId(), geofence.getId(), LocalDateTime.now(), "PENDING", new BigDecimal("50.00"), customer.getId(), store.getId());
        GeofenceOrderSummaryEntity savedSummary = geofenceOrderSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getOrderId()).isEqualTo(order.getId());
        assertThat(savedSummary.getGeofenceId()).isEqualTo(geofence.getId());
        assertThat(savedSummary.getCustomerId()).isEqualTo(customer.getId());
        assertThat(savedSummary.getStoreId()).isEqualTo(store.getId());

        Optional<GeofenceOrderSummaryEntity> foundSummary = geofenceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("PENDING");
        assertThat(foundSummary.get().getTotalAmount()).isEqualByComparingTo("50.00");
    }

    @Test
    void testUpdateGeofenceOrderSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        OrderEntity order = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceOrderSummaryEntity summary = new GeofenceOrderSummaryEntity(
                null, order.getId(), geofence.getId(), LocalDateTime.now(), "ACCEPTED", new BigDecimal("75.00"), customer.getId(), store.getId());
        GeofenceOrderSummaryEntity savedSummary = geofenceOrderSummaryRepository.save(summary);

        savedSummary.setStatus("DELIVERED");
        savedSummary.setTotalAmount(new BigDecimal("70.00"));
        geofenceOrderSummaryRepository.save(savedSummary);

        Optional<GeofenceOrderSummaryEntity> updatedSummary = geofenceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStatus()).isEqualTo("DELIVERED");
        assertThat(updatedSummary.get().getTotalAmount()).isEqualByComparingTo("70.00");
    }

    @Test
    void testDeleteGeofenceOrderSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        OrderEntity order = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        GeofenceEntity geofence = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        GeofenceOrderSummaryEntity summary = new GeofenceOrderSummaryEntity(
                null, order.getId(), geofence.getId(), LocalDateTime.now(), "CANCELLED", new BigDecimal("20.00"), customer.getId(), store.getId());
        GeofenceOrderSummaryEntity savedSummary = geofenceOrderSummaryRepository.save(summary);

        geofenceOrderSummaryRepository.deleteById(savedSummary.getId());
        Optional<GeofenceOrderSummaryEntity> deletedSummary = geofenceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByGeofenceId() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order1.getId(), geofence1.getId(), LocalDateTime.now(), "PENDING", new BigDecimal("10.00"), customer.getId(), store.getId()));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order2.getId(), geofence1.getId(), LocalDateTime.now(), "DELIVERED", new BigDecimal("25.00"), customer.getId(), store.getId()));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order3.getId(), geofence2.getId(), LocalDateTime.now(), "NEW", new BigDecimal("30.00"), customer.getId(), store.getId()));

        List<GeofenceOrderSummaryEntity> ordersForGeofence10 = geofenceOrderSummaryRepository.findByGeofenceId(geofence1.getId());
        assertThat(ordersForGeofence10).hasSize(2);
        assertThat(ordersForGeofence10.stream().allMatch(s -> s.getGeofenceId().equals(geofence1.getId()))).isTrue();
    }

    @Test
    void testFindByOrderId() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order1.getId(), geofence1.getId(), LocalDateTime.now(), "NEW", new BigDecimal("15.00"), customer.getId(), store.getId()));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order2.getId(), geofence2.getId(), LocalDateTime.now(), "ACCEPTED", new BigDecimal("18.00"), customer.getId(), store.getId()));

        Optional<GeofenceOrderSummaryEntity> foundSummary = geofenceOrderSummaryRepository.findByOrderId(order1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getGeofenceId()).isEqualTo(geofence1.getId());
    }

    @Test
    void testFindByCustomerId() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order1.getId(), geofence1.getId(), LocalDateTime.now(), "PENDING", new BigDecimal("100.00"), customer.getId(), store.getId()));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order2.getId(), geofence1.getId(), LocalDateTime.now(), "DELIVERED", new BigDecimal("120.00"), customer.getId(), store.getId()));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order3.getId(), geofence2.getId(), LocalDateTime.now(), "ACCEPTED", new BigDecimal("80.00"), customer.getId(), store.getId()));

        List<GeofenceOrderSummaryEntity> ordersForCustomer40 = geofenceOrderSummaryRepository.findByCustomerId(customer.getId());
        assertThat(ordersForCustomer40).hasSize(3);
        assertThat(ordersForCustomer40.stream().allMatch(s -> s.getCustomerId().equals(customer.getId()))).isTrue();
    }

    @Test
    void testFindByStoreId() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CustomerEntity customer = customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com"));
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, store.getId(), customer.getId(), LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        GeofenceEntity geofence1 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));
        GeofenceEntity geofence2 = geofenceRepository.save(new GeofenceEntity(null, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", "DELIVERY_ZONE", "Zone A", true));

        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order1.getId(), geofence1.getId(), LocalDateTime.now(), "PENDING", new BigDecimal("60.00"), customer.getId(), store.getId()));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order2.getId(), geofence1.getId(), LocalDateTime.now(), "DELIVERED", new BigDecimal("70.00"), customer.getId(), store.getId()));
        geofenceOrderSummaryRepository.save(new GeofenceOrderSummaryEntity(null, order3.getId(), geofence2.getId(), LocalDateTime.now(), "ACCEPTED", new BigDecimal("80.00"), customer.getId(), store.getId()));

        List<GeofenceOrderSummaryEntity> ordersForStore500 = geofenceOrderSummaryRepository.findByStoreId(store.getId());
        assertThat(ordersForStore500).hasSize(3);
        assertThat(ordersForStore500.stream().allMatch(s -> s.getStoreId().equals(store.getId()))).isTrue();
    }
}
