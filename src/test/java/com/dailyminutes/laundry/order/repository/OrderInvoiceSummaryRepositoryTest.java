package com.dailyminutes.laundry.order.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderInvoiceSummaryEntity;
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

/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.order.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.order.domain.model") // Updated package name
class OrderInvoiceSummaryRepositoryTest {

    @Autowired
    private OrderInvoiceSummaryRepository orderInvoiceSummaryRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void testSaveAndFindOrderInvoiceSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderInvoiceSummaryEntity summary = new OrderInvoiceSummaryEntity(
                null, order.getId(), 10l, LocalDateTime.now(), new BigDecimal("150.00"),
                new BigDecimal("15.00"), new BigDecimal("5.00"));
        OrderInvoiceSummaryEntity savedSummary = orderInvoiceSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getOrderId()).isEqualTo(order.getId());
        assertThat(savedSummary.getInvoiceId()).isEqualTo(10l);
        assertThat(savedSummary.getTotalPrice()).isEqualByComparingTo("150.00");

        Optional<OrderInvoiceSummaryEntity> foundSummary = orderInvoiceSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalTax()).isEqualByComparingTo("15.00");
    }

    @Test
    void testUpdateOrderInvoiceSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderInvoiceSummaryEntity summary = new OrderInvoiceSummaryEntity(
                null, order.getId(), 10l, LocalDateTime.now(), new BigDecimal("200.00"),
                new BigDecimal("20.00"), new BigDecimal("10.00"));
        OrderInvoiceSummaryEntity savedSummary = orderInvoiceSummaryRepository.save(summary);

        savedSummary.setTotalPrice(new BigDecimal("210.00"));
        savedSummary.setTotalDiscount(new BigDecimal("15.00"));
        orderInvoiceSummaryRepository.save(savedSummary);

        Optional<OrderInvoiceSummaryEntity> updatedSummary = orderInvoiceSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getTotalPrice()).isEqualByComparingTo("210.00");
        assertThat(updatedSummary.get().getTotalDiscount()).isEqualByComparingTo("15.00");
    }

    @Test
    void testDeleteOrderInvoiceSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderInvoiceSummaryEntity summary = new OrderInvoiceSummaryEntity(
                null, order.getId(), 10l, LocalDateTime.now(), new BigDecimal("50.00"),
                new BigDecimal("5.00"), new BigDecimal("0.00"));
        OrderInvoiceSummaryEntity savedSummary = orderInvoiceSummaryRepository.save(summary);

        orderInvoiceSummaryRepository.deleteById(savedSummary.getId());
        Optional<OrderInvoiceSummaryEntity> deletedSummary = orderInvoiceSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByOrderId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderInvoiceSummaryRepository.save(new OrderInvoiceSummaryEntity(null, order1.getId(), 10l, LocalDateTime.now(), new BigDecimal("100.00"), new BigDecimal("10.00"), new BigDecimal("0.00")));
        orderInvoiceSummaryRepository.save(new OrderInvoiceSummaryEntity(null, order2.getId(), 20l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("0.00")));

        Optional<OrderInvoiceSummaryEntity> foundSummary = orderInvoiceSummaryRepository.findByOrderId(order1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalPrice()).isEqualByComparingTo("100.00");
    }

    @Test
    void testFindByInvoiceId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderInvoiceSummaryRepository.save(new OrderInvoiceSummaryEntity(null, order1.getId(), 10l, LocalDateTime.now(), new BigDecimal("80.00"), new BigDecimal("8.00"), new BigDecimal("0.00")));
        orderInvoiceSummaryRepository.save(new OrderInvoiceSummaryEntity(null, order2.getId(), 20l, LocalDateTime.now(), new BigDecimal("90.00"), new BigDecimal("9.00"), new BigDecimal("0.00")));

        Optional<OrderInvoiceSummaryEntity> foundSummary = orderInvoiceSummaryRepository.findByInvoiceId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalPrice()).isEqualByComparingTo("80.00");
    }

    @Test
    void testFindByInvoiceDateBetween() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 31, 23, 59);
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));

        orderInvoiceSummaryRepository.save(new OrderInvoiceSummaryEntity(null, order1.getId(), 10l, LocalDateTime.of(2025, 1, 15, 10, 0), new BigDecimal("10.00"), new BigDecimal("1.00"), new BigDecimal("0.00")));
        orderInvoiceSummaryRepository.save(new OrderInvoiceSummaryEntity(null, order2.getId(), 20l, LocalDateTime.of(2025, 1, 20, 11, 0), new BigDecimal("20.00"), new BigDecimal("2.00"), new BigDecimal("0.00")));
        orderInvoiceSummaryRepository.save(new OrderInvoiceSummaryEntity(null, order3.getId(), 30l, LocalDateTime.of(2025, 2, 5, 12, 0), new BigDecimal("30.00"), new BigDecimal("3.00"), new BigDecimal("0.00"))); // Outside range

        List<OrderInvoiceSummaryEntity> summaries = orderInvoiceSummaryRepository.findByInvoiceDateBetween(start, end);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().map(OrderInvoiceSummaryEntity::getTotalPrice))
                .containsExactlyInAnyOrder(new BigDecimal("10.00"), new BigDecimal("20.00"));
    }
}
