package com.dailyminutes.laundry.payment.repository;

import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.domain.model.PaymentStatus;
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
 * The type Payment repository test.
 */
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.payment.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.payment.domain.model"})
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    // Helper method to generate unique transaction IDs for tests
    private String generateUniqueTransactionId() {
        return "TXN-" + UUID.randomUUID().toString();
    }

    /**
     * Test save and find payment.
     */
    @Test
    void testSaveAndFindPayment() {
        String transactionId = generateUniqueTransactionId();
        PaymentEntity payment = new PaymentEntity(null, 10l, 20l, transactionId, new BigDecimal("150.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CREDIT_CARD, "Online payment for order 1");
        PaymentEntity savedPayment = paymentRepository.save(payment);

        assertThat(savedPayment).isNotNull();
        assertThat(savedPayment.getId()).isNotNull();
        assertThat(savedPayment.getOrderId()).isEqualTo(10l);
        assertThat(savedPayment.getCustomerId()).isEqualTo(20l);
        assertThat(savedPayment.getTransactionId()).isEqualTo(transactionId);

        Optional<PaymentEntity> foundPayment = paymentRepository.findById(savedPayment.getId());
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getAmount()).isEqualByComparingTo("150.00");
        assertThat(foundPayment.get().getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    /**
     * Test update payment.
     */
    @Test
    void testUpdatePayment() {
        String transactionId = generateUniqueTransactionId();
        PaymentEntity payment = new PaymentEntity(null, 10l, 20l, transactionId, new BigDecimal("200.00"), LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.UPI, "UPI payment for order 2");
        PaymentEntity savedPayment = paymentRepository.save(payment);

        savedPayment.setStatus(PaymentStatus.COMPLETED);
        savedPayment.setRemarks("UPI payment successful");
        PaymentEntity updatedPayment = paymentRepository.save(savedPayment);

        Optional<PaymentEntity> foundUpdatedPayment = paymentRepository.findById(updatedPayment.getId());
        assertThat(foundUpdatedPayment).isPresent();
        assertThat(foundUpdatedPayment.get().getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(foundUpdatedPayment.get().getRemarks()).isEqualTo("UPI payment successful");
    }

    /**
     * Test delete payment.
     */
    @Test
    void testDeletePayment() {
        String transactionId = generateUniqueTransactionId();
        PaymentEntity payment = new PaymentEntity(null, 10l, 20l, transactionId, new BigDecimal("50.00"), LocalDateTime.now(), PaymentStatus.FAILED, PaymentMethod.CASH, "Cash payment for order 3");
        PaymentEntity savedPayment = paymentRepository.save(payment);

        paymentRepository.deleteById(savedPayment.getId());
        Optional<PaymentEntity> deletedPayment = paymentRepository.findById(savedPayment.getId());
        assertThat(deletedPayment).isNotPresent();
    }

    /**
     * Test find by order id.
     */
    @Test
    void testFindByOrderId() {
        String transactionId1 = generateUniqueTransactionId();
        String transactionId2 = generateUniqueTransactionId();
        paymentRepository.save(new PaymentEntity(null, 10l, 10l, transactionId1, new BigDecimal("10.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CREDIT_CARD, ""));
        paymentRepository.save(new PaymentEntity(null, 10l, 20l, transactionId2, new BigDecimal("20.00"), LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.DEBIT_CARD, ""));
        paymentRepository.save(new PaymentEntity(null, 30l, 30l, generateUniqueTransactionId(), new BigDecimal("30.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.UPI, ""));

        List<PaymentEntity> paymentsForOrder100 = paymentRepository.findByOrderId(10l);
        assertThat(paymentsForOrder100).hasSize(2);
        assertThat(paymentsForOrder100.stream().allMatch(p -> p.getOrderId().equals(10l))).isTrue();
    }

    /**
     * Test find by customer id.
     */
    @Test
    void testFindByCustomerId() {
        String transactionId3 = generateUniqueTransactionId();
        String transactionId4 = generateUniqueTransactionId();
        paymentRepository.save(new PaymentEntity(null, 10l, 10l, transactionId3, new BigDecimal("40.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.NET_BANKING, ""));
        paymentRepository.save(new PaymentEntity(null, 20l, 10l, transactionId4, new BigDecimal("50.00"), LocalDateTime.now(), PaymentStatus.REFUNDED, PaymentMethod.WALLET, ""));
        paymentRepository.save(new PaymentEntity(null, 30l, 30l, generateUniqueTransactionId(), new BigDecimal("60.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));

        List<PaymentEntity> paymentsForCustomer30 = paymentRepository.findByCustomerId(10l);
        assertThat(paymentsForCustomer30).hasSize(2);
        assertThat(paymentsForCustomer30.stream().allMatch(p -> p.getCustomerId().equals(10l))).isTrue();
    }

    /**
     * Test find by transaction id.
     */
    @Test
    void testFindByTransactionId() {
        String uniqueTxnId = generateUniqueTransactionId();
        paymentRepository.save(new PaymentEntity(null, 10l, 10l, uniqueTxnId, new BigDecimal("70.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.OTHER, ""));
        Optional<PaymentEntity> foundPayment = paymentRepository.findByTransactionId(uniqueTxnId);
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getAmount()).isEqualByComparingTo("70.00");
    }

    /**
     * Test find by status.
     */
    @Test
    void testFindByStatus() {
        paymentRepository.save(new PaymentEntity(null, 10l, 10l, generateUniqueTransactionId(), new BigDecimal("80.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CREDIT_CARD, ""));
        paymentRepository.save(new PaymentEntity(null, 20l, 20l, generateUniqueTransactionId(), new BigDecimal("90.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.DEBIT_CARD, ""));
        paymentRepository.save(new PaymentEntity(null, 30l, 30l, generateUniqueTransactionId(), new BigDecimal("100.00"), LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.UPI, ""));

        List<PaymentEntity> completedPayments = paymentRepository.findByStatus(PaymentStatus.COMPLETED);
        assertThat(completedPayments).hasSize(2);
        assertThat(completedPayments.stream().allMatch(p -> p.getStatus().equals(PaymentStatus.COMPLETED))).isTrue();
    }

    /**
     * Test find by method.
     */
    @Test
    void testFindByMethod() {
        paymentRepository.save(new PaymentEntity(null, 10l, 10l, generateUniqueTransactionId(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentRepository.save(new PaymentEntity(null, 20l, 20l, generateUniqueTransactionId(), new BigDecimal("120.00"), LocalDateTime.now(), PaymentStatus.PENDING, PaymentMethod.CASH, ""));
        paymentRepository.save(new PaymentEntity(null, 30l, 30l, generateUniqueTransactionId(), new BigDecimal("130.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CREDIT_CARD, ""));

        List<PaymentEntity> cashPayments = paymentRepository.findByMethod(PaymentMethod.CASH);
        assertThat(cashPayments).hasSize(2);
        assertThat(cashPayments.stream().allMatch(p -> p.getMethod().equals(PaymentMethod.CASH))).isTrue();
    }
}
