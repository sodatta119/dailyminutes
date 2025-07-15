package com.dailyminutes.laundry.payment.repository; // Updated package name

import com.dailyminutes.DailyminutesApplication; // Updated import
import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentMethod;
import com.dailyminutes.laundry.payment.domain.model.PaymentOrderSummaryEntity; // Updated import
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
import java.util.concurrent.atomic.AtomicLong; // For generating unique IDs

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.payment.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.payment.domain.model") // Updated package name
class PaymentOrderSummaryRepositoryTest {

    @Autowired
    private PaymentOrderSummaryRepository paymentOrderSummaryRepository;

    @Autowired
    private PaymentRepository paymentRepository;



    @Test
    void testSaveAndFindPaymentOrderSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentOrderSummaryEntity summary = new PaymentOrderSummaryEntity(
                null, payment.getId(), 10l, LocalDateTime.now(), "COMPLETED", new BigDecimal("150.00"), 10l, 10l);
        PaymentOrderSummaryEntity savedSummary = paymentOrderSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getPaymentId()).isEqualTo(payment.getId());
        assertThat(savedSummary.getOrderId()).isEqualTo(10l);
        assertThat(savedSummary.getCustomerId()).isEqualTo(10l);
        assertThat(savedSummary.getStoreId()).isEqualTo(10l);
        assertThat(savedSummary.getStatus()).isEqualTo("COMPLETED");

        Optional<PaymentOrderSummaryEntity> foundSummary = paymentOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalAmount()).isEqualByComparingTo("150.00");
    }

    @Test
    void testUpdatePaymentOrderSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));

        PaymentOrderSummaryEntity summary = new PaymentOrderSummaryEntity(
                null, payment.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("200.00"), 10l, 10l);
        PaymentOrderSummaryEntity savedSummary = paymentOrderSummaryRepository.save(summary);

        savedSummary.setStatus("DELIVERED");
        savedSummary.setTotalAmount(new BigDecimal("210.00"));
        paymentOrderSummaryRepository.save(savedSummary);

        Optional<PaymentOrderSummaryEntity> updatedSummary = paymentOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStatus()).isEqualTo("DELIVERED");
        assertThat(updatedSummary.get().getTotalAmount()).isEqualByComparingTo("210.00");
    }

    @Test
    void testDeletePaymentOrderSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));

        PaymentOrderSummaryEntity summary = new PaymentOrderSummaryEntity(
                null, payment.getId(), 10l, LocalDateTime.now(), "CANCELLED", new BigDecimal("50.00"), 10l, 10l);
        PaymentOrderSummaryEntity savedSummary = paymentOrderSummaryRepository.save(summary);

        paymentOrderSummaryRepository.deleteById(savedSummary.getId());
        Optional<PaymentOrderSummaryEntity> deletedSummary = paymentOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByPaymentId() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn1", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 20l, 20l, "test-trn2", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment1.getId(), 10l, LocalDateTime.now(), "COMPLETED", new BigDecimal("100.00"), 10l, 10l));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment2.getId(), 20l, LocalDateTime.now(), "PENDING", new BigDecimal("50.00"), 20l, 20l));

        Optional<PaymentOrderSummaryEntity> foundSummary = paymentOrderSummaryRepository.findByPaymentId(payment1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void testFindByOrderId() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn1", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 20l, 20l, "test-trn2", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment1.getId(), 10l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("70.00"), 10l, 10l));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment2.getId(), 20l, LocalDateTime.now(), "DELIVERED", new BigDecimal("90.00"), 20l, 20l));

        Optional<PaymentOrderSummaryEntity> foundSummary = paymentOrderSummaryRepository.findByOrderId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("ACCEPTED");
    }

    @Test
    void testFindByCustomerId() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn1", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 20l, 20l, "test-trn2", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment3 = paymentRepository.save(new PaymentEntity(null, 30l, 20l, "test-trn3", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment1.getId(), 10l, LocalDateTime.now(), "COMPLETED", new BigDecimal("110.00"), 10l, 10l));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment2.getId(), 20l, LocalDateTime.now(), "PENDING", new BigDecimal("120.00"), 10l, 10l));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment3.getId(), 30l, LocalDateTime.now(), "COMPLETED", new BigDecimal("130.00"), 30l, 30l));

        List<PaymentOrderSummaryEntity> summaries = paymentOrderSummaryRepository.findByCustomerId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getCustomerId().equals(10l))).isTrue();
    }

    @Test
    void testFindByStoreId() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn1", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn2", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment3 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn3", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment1.getId(), 10l, LocalDateTime.now(), "COMPLETED", new BigDecimal("140.00"), 10l, 10l));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment2.getId(), 20l, LocalDateTime.now(), "PENDING", new BigDecimal("150.00"), 20l, 10l));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment3.getId(), 30l, LocalDateTime.now(), "COMPLETED", new BigDecimal("160.00"), 30l, 30l));

        List<PaymentOrderSummaryEntity> summaries = paymentOrderSummaryRepository.findByStoreId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getStoreId().equals(10l))).isTrue();
    }

    @Test
    void testFindByStatus() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, "test-trn1", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 20l, 20l, "test-trn2", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment3 = paymentRepository.save(new PaymentEntity(null, 30l, 20l, "test-trn3", new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment1.getId(), 10l, LocalDateTime.now(), "COMPLETED", new BigDecimal("25.00"), 10l, 10l));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment2.getId(), 20l, LocalDateTime.now(), "COMPLETED", new BigDecimal("35.00"), 20l, 20l));
        paymentOrderSummaryRepository.save(new PaymentOrderSummaryEntity(null, payment3.getId(), 30l, LocalDateTime.now(), "PENDING", new BigDecimal("45.00"), 30l, 30l));

        List<PaymentOrderSummaryEntity> completedSummaries = paymentOrderSummaryRepository.findByStatus("COMPLETED");
        assertThat(completedSummaries).hasSize(2);
        assertThat(completedSummaries.stream().allMatch(s -> s.getStatus().equals("COMPLETED"))).isTrue();
    }
}
