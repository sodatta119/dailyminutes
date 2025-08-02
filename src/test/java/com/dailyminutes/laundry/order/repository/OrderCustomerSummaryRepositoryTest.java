package com.dailyminutes.laundry.order.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.order.domain.model.OrderCustomerSummaryEntity;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.order.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.order.domain.model") // Updated package name
class OrderCustomerSummaryRepositoryTest {

    @Autowired
    private OrderCustomerSummaryRepository orderCustomerSummaryRepository;

    @Autowired
    private OrderRepository orderRepository;


    private String generateUniquePhoneNumber() {
        return "9" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    private String generateUniqueEmail() {
        return "test_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    @Test
    void testSaveAndFindOrderCustomerSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null,  10L, 10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String phoneNumber = generateUniquePhoneNumber();
        String email = generateUniqueEmail();

        OrderCustomerSummaryEntity summary = new OrderCustomerSummaryEntity(
                null, order.getId(), 10l, "Customer A", phoneNumber, email);
        OrderCustomerSummaryEntity savedSummary = orderCustomerSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getOrderId()).isEqualTo(order.getId());
        assertThat(savedSummary.getCustomerId()).isEqualTo(10l);
        assertThat(savedSummary.getCustomerName()).isEqualTo("Customer A");
        assertThat(savedSummary.getCustomerPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedSummary.getCustomerEmail()).isEqualTo(email);

        Optional<OrderCustomerSummaryEntity> foundSummary = orderCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Customer A");
    }

    @Test
    void testUpdateOrderCustomerSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10L,10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String phoneNumber = generateUniquePhoneNumber();
        String email = generateUniqueEmail();

        OrderCustomerSummaryEntity summary = new OrderCustomerSummaryEntity(
                null, order.getId(), 10l, "Customer B", phoneNumber, email);
        OrderCustomerSummaryEntity savedSummary = orderCustomerSummaryRepository.save(summary);

        savedSummary.setCustomerName("Customer B Updated");
        savedSummary.setCustomerEmail(generateUniqueEmail());
        orderCustomerSummaryRepository.save(savedSummary);

        Optional<OrderCustomerSummaryEntity> updatedSummary = orderCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getCustomerName()).isEqualTo("Customer B Updated");
        assertThat(updatedSummary.get().getCustomerEmail()).isEqualTo(savedSummary.getCustomerEmail());
    }

    @Test
    void testDeleteOrderCustomerSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null,  10L, 10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String phoneNumber = generateUniquePhoneNumber();
        String email = generateUniqueEmail();

        OrderCustomerSummaryEntity summary = new OrderCustomerSummaryEntity(
                null, order.getId(), 10l, "Customer C", phoneNumber, email);
        OrderCustomerSummaryEntity savedSummary = orderCustomerSummaryRepository.save(summary);

        orderCustomerSummaryRepository.deleteById(savedSummary.getId());
        Optional<OrderCustomerSummaryEntity> deletedSummary = orderCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByOrderId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10L, 10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10L, 10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderCustomerSummaryRepository.save(new OrderCustomerSummaryEntity(null, order1.getId(), 10l, "Cust D", generateUniquePhoneNumber(), generateUniqueEmail()));
        orderCustomerSummaryRepository.save(new OrderCustomerSummaryEntity(null, order2.getId(), 20l, "Cust E", generateUniquePhoneNumber(), generateUniqueEmail()));

        Optional<OrderCustomerSummaryEntity> foundSummary = orderCustomerSummaryRepository.findByOrderId(order1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Cust D");
    }

    @Test
    void testFindByCustomerId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10L, 10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10L, 10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, 10L, 10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderCustomerSummaryRepository.save(new OrderCustomerSummaryEntity(null, order1.getId(), 10l, "Cust F", generateUniquePhoneNumber(), generateUniqueEmail()));
        orderCustomerSummaryRepository.save(new OrderCustomerSummaryEntity(null, order2.getId(), 10l, "Cust G", generateUniquePhoneNumber(), generateUniqueEmail())); // Same customer, different order
        orderCustomerSummaryRepository.save(new OrderCustomerSummaryEntity(null, order3.getId(), 20l, "Cust H", generateUniquePhoneNumber(), generateUniqueEmail()));

        List<OrderCustomerSummaryEntity> summariesForCustomer = orderCustomerSummaryRepository.findByCustomerId(10l);
        assertThat(summariesForCustomer).hasSize(2);
        assertThat(summariesForCustomer.stream().allMatch(s -> s.getCustomerId().equals(10l))).isTrue();
    }

    @Test
    void testFindByCustomerPhoneNumber() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10L, 10L, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String phoneNumber = generateUniquePhoneNumber();
        orderCustomerSummaryRepository.save(new OrderCustomerSummaryEntity(null, order.getId(), 10l, "Cust I", phoneNumber, generateUniqueEmail()));

        Optional<OrderCustomerSummaryEntity> foundSummary = orderCustomerSummaryRepository.findByCustomerPhoneNumber(phoneNumber);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Cust I");
    }
}
