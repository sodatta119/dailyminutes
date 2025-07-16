package com.dailyminutes.laundry.payment.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.payment.domain.model.PaymentEntity;
import com.dailyminutes.laundry.payment.domain.model.PaymentInvoiceSummaryEntity;
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
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.payment.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.payment.domain.model") // Updated package name
class PaymentInvoiceSummaryRepositoryTest {

    @Autowired
    private PaymentInvoiceSummaryRepository paymentInvoiceSummaryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private String generateUniqueTransactionNumber() {
        return "TRN_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    @Test
    void testSaveAndFindPaymentInvoiceSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentInvoiceSummaryEntity summary = new PaymentInvoiceSummaryEntity(
                null, payment.getId(), 10l, LocalDateTime.now(), new BigDecimal("150.00"),
                new BigDecimal("15.00"), new BigDecimal("5.00"));
        PaymentInvoiceSummaryEntity savedSummary = paymentInvoiceSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getPaymentId()).isEqualTo(payment.getId());
        assertThat(savedSummary.getInvoiceId()).isEqualTo(10l);
        assertThat(savedSummary.getTotalPrice()).isEqualByComparingTo("150.00");

        Optional<PaymentInvoiceSummaryEntity> foundSummary = paymentInvoiceSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalTax()).isEqualByComparingTo("15.00");
    }

    @Test
    void testUpdatePaymentInvoiceSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentInvoiceSummaryEntity summary = new PaymentInvoiceSummaryEntity(
                null, payment.getId(), 10l, LocalDateTime.now(), new BigDecimal("200.00"),
                new BigDecimal("20.00"), new BigDecimal("10.00"));
        PaymentInvoiceSummaryEntity savedSummary = paymentInvoiceSummaryRepository.save(summary);

        savedSummary.setTotalPrice(new BigDecimal("210.00"));
        savedSummary.setTotalDiscount(new BigDecimal("15.00"));
        paymentInvoiceSummaryRepository.save(savedSummary);

        Optional<PaymentInvoiceSummaryEntity> updatedSummary = paymentInvoiceSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getTotalPrice()).isEqualByComparingTo("210.00");
        assertThat(updatedSummary.get().getTotalDiscount()).isEqualByComparingTo("15.00");
    }

    @Test
    void testDeletePaymentInvoiceSummary() {
        PaymentEntity payment = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentInvoiceSummaryEntity summary = new PaymentInvoiceSummaryEntity(
                null, payment.getId(), 10l, LocalDateTime.now(), new BigDecimal("50.00"),
                new BigDecimal("5.00"), new BigDecimal("0.00"));
        PaymentInvoiceSummaryEntity savedSummary = paymentInvoiceSummaryRepository.save(summary);

        paymentInvoiceSummaryRepository.deleteById(savedSummary.getId());
        Optional<PaymentInvoiceSummaryEntity> deletedSummary = paymentInvoiceSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByPaymentId() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentInvoiceSummaryRepository.save(new PaymentInvoiceSummaryEntity(null, payment1.getId(), 10l, LocalDateTime.now(), new BigDecimal("100.00"), new BigDecimal("10.00"), new BigDecimal("0.00")));
        paymentInvoiceSummaryRepository.save(new PaymentInvoiceSummaryEntity(null, payment1.getId(), 20l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("0.00")));
        paymentInvoiceSummaryRepository.save(new PaymentInvoiceSummaryEntity(null, payment2.getId(), 30l, LocalDateTime.now(), new BigDecimal("75.00"), new BigDecimal("7.50"), new BigDecimal("0.00")));

        List<PaymentInvoiceSummaryEntity> summaries = paymentInvoiceSummaryRepository.findByPaymentId(payment1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getPaymentId().equals(payment1.getId()))).isTrue();
    }

    @Test
    void testFindByInvoiceId() {
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        paymentInvoiceSummaryRepository.save(new PaymentInvoiceSummaryEntity(null, payment1.getId(), 10l, LocalDateTime.now(), new BigDecimal("80.00"), new BigDecimal("8.00"), new BigDecimal("0.00")));
        paymentInvoiceSummaryRepository.save(new PaymentInvoiceSummaryEntity(null, payment2.getId(), 20l, LocalDateTime.now(), new BigDecimal("90.00"), new BigDecimal("9.00"), new BigDecimal("0.00")));

        Optional<PaymentInvoiceSummaryEntity> foundSummary = paymentInvoiceSummaryRepository.findByInvoiceId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalPrice()).isEqualByComparingTo("80.00");
    }

    @Test
    void testFindByInvoiceDateBetween() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 31, 23, 59);
        PaymentEntity payment1 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment2 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));
        PaymentEntity payment3 = paymentRepository.save(new PaymentEntity(null, 10l, 20l, generateUniqueTransactionNumber(), new BigDecimal("110.00"), LocalDateTime.now(), PaymentStatus.COMPLETED, PaymentMethod.CASH, ""));

        paymentInvoiceSummaryRepository.save(new PaymentInvoiceSummaryEntity(null, payment1.getId(), 10l, LocalDateTime.of(2025, 1, 15, 10, 0), new BigDecimal("10.00"), new BigDecimal("1.00"), new BigDecimal("0.00")));
        paymentInvoiceSummaryRepository.save(new PaymentInvoiceSummaryEntity(null, payment2.getId(), 20l, LocalDateTime.now(), new BigDecimal("20.00"), new BigDecimal("2.00"), new BigDecimal("0.00"))); // Current time, might fall outside if test runs near month end
        paymentInvoiceSummaryRepository.save(new PaymentInvoiceSummaryEntity(null, payment3.getId(), 30l, LocalDateTime.of(2025, 1, 5, 12, 0), new BigDecimal("30.00"), new BigDecimal("3.00"), new BigDecimal("0.00"))); // Outside range

        List<PaymentInvoiceSummaryEntity> summaries = paymentInvoiceSummaryRepository.findByInvoiceDateBetween(start, end);
        assertThat(summaries).hasSize(2); // Assuming the current time falls within January for this test run
        assertThat(summaries.stream().map(PaymentInvoiceSummaryEntity::getTotalPrice))
                .contains(new BigDecimal("10.00")); // Check for specific values
    }
}
