package com.dailyminutes.laundry.order.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderStatus;
import com.dailyminutes.laundry.order.domain.model.OrderTaskSummaryEntity;
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
 * @version 15/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.order.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.order.domain.model") // Updated package name
class OrderTaskSummaryRepositoryTest {

    @Autowired
    private OrderTaskSummaryRepository orderTaskSummaryRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void testSaveAndFindOrderTaskSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));

        OrderTaskSummaryEntity summary = new OrderTaskSummaryEntity(
                null, order.getId(), 10l, "PICKUP", "ASSIGNED", LocalDateTime.now(), 10l, "Agent Alpha");
        OrderTaskSummaryEntity savedSummary = orderTaskSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getOrderId()).isEqualTo(order.getId());
        assertThat(savedSummary.getTaskId()).isEqualTo(10l);
        assertThat(savedSummary.getTaskType()).isEqualTo("PICKUP");
        assertThat(savedSummary.getTaskStatus()).isEqualTo("ASSIGNED");
        assertThat(savedSummary.getAgentId()).isEqualTo(10l);
        assertThat(savedSummary.getAgentName()).isEqualTo("Agent Alpha");

        Optional<OrderTaskSummaryEntity> foundSummary = orderTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskType()).isEqualTo("PICKUP");
    }

    @Test
    void testUpdateOrderTaskSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));

        OrderTaskSummaryEntity summary = new OrderTaskSummaryEntity(
                null, order.getId(), 10l, "DELIVERY", "STARTED", LocalDateTime.now(), 10l, "Agent Beta");
        OrderTaskSummaryEntity savedSummary = orderTaskSummaryRepository.save(summary);

        savedSummary.setTaskStatus("SUCCESSFUL");
        savedSummary.setAgentName("Agent Beta (Completed)");
        orderTaskSummaryRepository.save(savedSummary);

        Optional<OrderTaskSummaryEntity> updatedSummary = orderTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getTaskStatus()).isEqualTo("SUCCESSFUL");
        assertThat(updatedSummary.get().getAgentName()).isEqualTo("Agent Beta (Completed)");
    }

    @Test
    void testDeleteOrderTaskSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));

        OrderTaskSummaryEntity summary = new OrderTaskSummaryEntity(
                null, order.getId(), 10l, "PROCESS", "NEW", LocalDateTime.now(), 10l, "Agent Gamma");
        OrderTaskSummaryEntity savedSummary = orderTaskSummaryRepository.save(summary);

        orderTaskSummaryRepository.deleteById(savedSummary.getId());
        Optional<OrderTaskSummaryEntity> deletedSummary = orderTaskSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByOrderId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent D"));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order2.getId(), 20l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 20l, "Agent E"));

        Optional<OrderTaskSummaryEntity> foundSummary = orderTaskSummaryRepository.findByOrderId(order1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskType()).isEqualTo("PICKUP");
    }

    @Test
    void testFindByTaskId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order1.getId(), 10l, "PROCESS", "STARTED", LocalDateTime.now(), 10l, "Agent F"));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order2.getId(), 20l, "PICKUP", "COMPLETED", LocalDateTime.now(), 20l, "Agent G"));

        Optional<OrderTaskSummaryEntity> foundSummary = orderTaskSummaryRepository.findByTaskId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTaskStatus()).isEqualTo("STARTED");
    }

    @Test
    void testFindByAgentId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order1.getId(), 10l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 10l, "Agent H"));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order2.getId(), 20l, "PICKUP", "STARTED", LocalDateTime.now(), 10l, "Agent I"));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order3.getId(), 30l, "PROCESS", "NEW", LocalDateTime.now(), 20l, "Agent J"));

        List<OrderTaskSummaryEntity> summariesForAgent = orderTaskSummaryRepository.findByAgentId(10l);
        assertThat(summariesForAgent).hasSize(2);
        assertThat(summariesForAgent.stream().allMatch(s -> s.getAgentId().equals(10l))).isTrue();
    }

    @Test
    void testFindByTaskStatus() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order1.getId(), 10l, "PICKUP", "NEW", LocalDateTime.now(), 10l, "Agent K"));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order2.getId(), 20l, "NEW", "NEW", LocalDateTime.now(), 20l, "Agent L"));
        orderTaskSummaryRepository.save(new OrderTaskSummaryEntity(null, order3.getId(), 30l, "DELIVERY", "ASSIGNED", LocalDateTime.now(), 30l, "Agent M"));

        List<OrderTaskSummaryEntity> newTasks = orderTaskSummaryRepository.findByTaskStatus("NEW");
        assertThat(newTasks).hasSize(2);
        assertThat(newTasks.stream().allMatch(s -> s.getTaskStatus().equals("NEW"))).isTrue();
    }
}
