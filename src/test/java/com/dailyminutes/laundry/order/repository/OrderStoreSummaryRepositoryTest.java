package com.dailyminutes.laundry.order.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.dailyminutes.laundry.order.domain.model.OrderStoreSummaryEntity;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 12/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.order.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.order.domain.model") // Updated package name
class OrderStoreSummaryRepositoryTest {

    @Autowired
    private OrderStoreSummaryRepository orderStoreSummaryRepository;

    @Autowired
    private OrderRepository orderRepository;


    private String generateUniqueContactNumber() {
        return "9" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    private String generateUniqueEmail() {
        return "store_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    @Test
    void testSaveAndFindOrderStoreSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String contactNumber = generateUniqueContactNumber();
        String email = generateUniqueEmail();

        OrderStoreSummaryEntity summary = new OrderStoreSummaryEntity(
                null, order.getId(), 10l, "Main Store A", "123 Main St", contactNumber, email);
        OrderStoreSummaryEntity savedSummary = orderStoreSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getOrderId()).isEqualTo(order.getId());
        assertThat(savedSummary.getStoreId()).isEqualTo(10l);
        assertThat(savedSummary.getStoreName()).isEqualTo("Main Store A");
        assertThat(savedSummary.getStoreContactNumber()).isEqualTo(contactNumber);
        assertThat(savedSummary.getStoreEmail()).isEqualTo(email);

        Optional<OrderStoreSummaryEntity> foundSummary = orderStoreSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreName()).isEqualTo("Main Store A");
    }

    @Test
    void testUpdateOrderStoreSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String contactNumber = generateUniqueContactNumber();
        String email = generateUniqueEmail();

        OrderStoreSummaryEntity summary = new OrderStoreSummaryEntity(
                null, order.getId(), 10l, "Branch Store B", "456 Oak Ave", contactNumber, email);
        OrderStoreSummaryEntity savedSummary = orderStoreSummaryRepository.save(summary);

        savedSummary.setStoreName("Branch Store B Updated");
        savedSummary.setStoreEmail(generateUniqueEmail());
        orderStoreSummaryRepository.save(savedSummary);

        Optional<OrderStoreSummaryEntity> updatedSummary = orderStoreSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStoreName()).isEqualTo("Branch Store B Updated");
        assertThat(updatedSummary.get().getStoreEmail()).isEqualTo(savedSummary.getStoreEmail());
    }

    @Test
    void testDeleteOrderStoreSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String contactNumber = generateUniqueContactNumber();
        String email = generateUniqueEmail();

        OrderStoreSummaryEntity summary = new OrderStoreSummaryEntity(
                null, order.getId(), 10l, "Store C", "789 Pine Ln", contactNumber, email);
        OrderStoreSummaryEntity savedSummary = orderStoreSummaryRepository.save(summary);

        orderStoreSummaryRepository.deleteById(savedSummary.getId());
        Optional<OrderStoreSummaryEntity> deletedSummary = orderStoreSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByOrderId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderStoreSummaryRepository.save(new OrderStoreSummaryEntity(null, order1.getId(), 10l, "Store D", "Addr D", generateUniqueContactNumber(), generateUniqueEmail()));
        orderStoreSummaryRepository.save(new OrderStoreSummaryEntity(null, order2.getId(), 20l, "Store E", "Addr E", generateUniqueContactNumber(), generateUniqueEmail()));

        Optional<OrderStoreSummaryEntity> foundSummary = orderStoreSummaryRepository.findByOrderId(order1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreName()).isEqualTo("Store D");
    }

    @Test
    void testFindByStoreId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderStoreSummaryRepository.save(new OrderStoreSummaryEntity(null, order1.getId(), 10l, "Store F", "Addr F", generateUniqueContactNumber(), generateUniqueEmail()));
        orderStoreSummaryRepository.save(new OrderStoreSummaryEntity(null, order2.getId(), 10l, "Store G", "Addr G", generateUniqueContactNumber(), generateUniqueEmail())); // Same store, different order
        orderStoreSummaryRepository.save(new OrderStoreSummaryEntity(null, order3.getId(), 20l, "Store H", "Addr H", generateUniqueContactNumber(), generateUniqueEmail()));

        List<OrderStoreSummaryEntity> summariesForStore = orderStoreSummaryRepository.findByStoreId(10l);
        assertThat(summariesForStore).hasSize(2);
        assertThat(summariesForStore.stream().allMatch(s -> s.getStoreId().equals(10l))).isTrue();
    }

    @Test
    void testFindByStoreName() {
        String storeName = "Unique Store Name I";
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderStoreSummaryRepository.save(new OrderStoreSummaryEntity(null, order.getId(), 10l, storeName, "Addr I", generateUniqueContactNumber(), generateUniqueEmail()));

        Optional<OrderStoreSummaryEntity> foundSummary = orderStoreSummaryRepository.findByStoreName(storeName);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreAddress()).isEqualTo("Addr I");
    }
}
