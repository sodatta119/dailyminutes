package com.dailyminutes.laundry.order.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.order.domain.model.OrderEntity;
import com.dailyminutes.laundry.order.domain.model.OrderPaymentSummaryEntity;
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
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.order.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.order.domain.model") // Updated package name
class OrderPaymentSummaryRepositoryTest {

    @Autowired
    private OrderPaymentSummaryRepository orderPaymentSummaryRepository;

    @Autowired
    private OrderRepository orderRepository;


    private String generateUniqueTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    void testSaveAndFindOrderPaymentSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String transactionId = generateUniqueTransactionId();
        OrderPaymentSummaryEntity summary = new OrderPaymentSummaryEntity(
                null, order.getId(), 10l, LocalDateTime.now(), new BigDecimal("150.00"),
                "COMPLETED", "CREDIT_CARD", transactionId);
        OrderPaymentSummaryEntity savedSummary = orderPaymentSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getOrderId()).isEqualTo(order.getId());
        assertThat(savedSummary.getPaymentId()).isEqualTo(10l);
        assertThat(savedSummary.getTransactionId()).isEqualTo(transactionId);

        Optional<OrderPaymentSummaryEntity> foundSummary = orderPaymentSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAmount()).isEqualByComparingTo("150.00");
        assertThat(foundSummary.get().getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void testUpdateOrderPaymentSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String transactionId = generateUniqueTransactionId();
        OrderPaymentSummaryEntity summary = new OrderPaymentSummaryEntity(
                null, order.getId(), 10l, LocalDateTime.now(), new BigDecimal("200.00"),
                "PENDING", "UPI", transactionId);
        OrderPaymentSummaryEntity savedSummary = orderPaymentSummaryRepository.save(summary);

        savedSummary.setStatus("REFUNDED");
        savedSummary.setAmount(new BigDecimal("100.00")); // Partial refund
        orderPaymentSummaryRepository.save(savedSummary);

        Optional<OrderPaymentSummaryEntity> updatedSummary = orderPaymentSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStatus()).isEqualTo("REFUNDED");
        assertThat(updatedSummary.get().getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void testDeleteOrderPaymentSummary() {
        OrderEntity order = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        String transactionId = generateUniqueTransactionId();
        OrderPaymentSummaryEntity summary = new OrderPaymentSummaryEntity(
                null, order.getId(), 10l, LocalDateTime.now(), new BigDecimal("50.00"),
                "FAILED", "CASH", transactionId);
        OrderPaymentSummaryEntity savedSummary = orderPaymentSummaryRepository.save(summary);

        orderPaymentSummaryRepository.deleteById(savedSummary.getId());
        Optional<OrderPaymentSummaryEntity> deletedSummary = orderPaymentSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByOrderId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order1.getId(), 10l, LocalDateTime.now(), new BigDecimal("100.00"), "COMPLETED", "CREDIT_CARD", generateUniqueTransactionId()));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order1.getId(), 20l, LocalDateTime.now(), new BigDecimal("50.00"), "COMPLETED", "UPI", generateUniqueTransactionId()));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order2.getId(), 30l, LocalDateTime.now(), new BigDecimal("75.00"), "PENDING", "NET_BANKING", generateUniqueTransactionId()));

        List<OrderPaymentSummaryEntity> paymentsForOrder = orderPaymentSummaryRepository.findByOrderId(order1.getId());
        assertThat(paymentsForOrder).hasSize(2);
        assertThat(paymentsForOrder.stream().allMatch(s -> s.getOrderId().equals(order1.getId()))).isTrue();
    }

    @Test
    void testFindByPaymentId() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order1.getId(), 10l, LocalDateTime.now(), new BigDecimal("80.00"), "COMPLETED", "DEBIT_CARD", generateUniqueTransactionId()));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order2.getId(), 20l, LocalDateTime.now(), new BigDecimal("90.00"), "PENDING", "WALLET", generateUniqueTransactionId()));

        Optional<OrderPaymentSummaryEntity> foundSummary = orderPaymentSummaryRepository.findByPaymentId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAmount()).isEqualByComparingTo("80.00");
    }

    @Test
    void testFindByStatus() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order1.getId(), 10l, LocalDateTime.now(), new BigDecimal("25.00"), "COMPLETED", "CASH", generateUniqueTransactionId()));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order2.getId(), 20l, LocalDateTime.now(), new BigDecimal("35.00"), "COMPLETED", "CREDIT_CARD", generateUniqueTransactionId()));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order3.getId(), 30l, LocalDateTime.now(), new BigDecimal("45.00"), "PENDING", "UPI", generateUniqueTransactionId()));

        List<OrderPaymentSummaryEntity> completedSummaries = orderPaymentSummaryRepository.findByStatus("COMPLETED");
        assertThat(completedSummaries).hasSize(2);
        assertThat(completedSummaries.stream().allMatch(s -> s.getStatus().equals("COMPLETED"))).isTrue();
    }

    @Test
    void testFindByMethod() {
        OrderEntity order1 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order2 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        OrderEntity order3 = orderRepository.save(new OrderEntity(null, 10l, 10l, LocalDateTime.now(), OrderStatus.PENDING, new BigDecimal("25.50")));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order1.getId(), 10l, LocalDateTime.now(), new BigDecimal("55.00"), "COMPLETED", "NET_BANKING", generateUniqueTransactionId()));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order2.getId(), 20l, LocalDateTime.now(), new BigDecimal("65.00"), "PENDING", "NET_BANKING", generateUniqueTransactionId()));
        orderPaymentSummaryRepository.save(new OrderPaymentSummaryEntity(null, order3.getId(), 30l, LocalDateTime.now(), new BigDecimal("75.00"), "COMPLETED", "CASH", generateUniqueTransactionId()));

        List<OrderPaymentSummaryEntity> netBankingSummaries = orderPaymentSummaryRepository.findByMethod("NET_BANKING");
        assertThat(netBankingSummaries).hasSize(2);
        assertThat(netBankingSummaries.stream().allMatch(s -> s.getMethod().equals("NET_BANKING"))).isTrue();
    }
}
