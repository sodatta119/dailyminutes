package com.dailyminutes.laundry.invoice.repository; // Updated package name

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import com.dailyminutes.laundry.invoice.domain.model.InvoicePaymentSummaryEntity;
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
 * @version 12/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.invoice.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.invoice.domain.model") // Updated package name
class InvoicePaymentSummaryRepositoryTest {

    @Autowired
    private InvoicePaymentSummaryRepository invoicePaymentSummaryRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;


    private String generateUniqueTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    void testSaveAndFindInvoicePaymentSummary() {
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        String transactionId = generateUniqueTransactionId();
        InvoicePaymentSummaryEntity summary = new InvoicePaymentSummaryEntity(
                null, invoice.getId(), 10l, LocalDateTime.now(), new BigDecimal("150.00"),
                "COMPLETED", "CREDIT_CARD", transactionId);
        InvoicePaymentSummaryEntity savedSummary = invoicePaymentSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getInvoiceId()).isEqualTo(invoice.getId());
        assertThat(savedSummary.getPaymentId()).isEqualTo(10l);
        assertThat(savedSummary.getTransactionId()).isEqualTo(transactionId);

        Optional<InvoicePaymentSummaryEntity> foundSummary = invoicePaymentSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAmount()).isEqualByComparingTo("150.00");
        assertThat(foundSummary.get().getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void testUpdateInvoicePaymentSummary() {
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        String transactionId = generateUniqueTransactionId();
        InvoicePaymentSummaryEntity summary = new InvoicePaymentSummaryEntity(
                null, invoice.getId(), 10l, LocalDateTime.now(), new BigDecimal("200.00"),
                "PENDING", "UPI", transactionId);
        InvoicePaymentSummaryEntity savedSummary = invoicePaymentSummaryRepository.save(summary);

        savedSummary.setStatus("REFUNDED");
        savedSummary.setAmount(new BigDecimal("100.00")); // Partial refund
        invoicePaymentSummaryRepository.save(savedSummary);

        Optional<InvoicePaymentSummaryEntity> updatedSummary = invoicePaymentSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStatus()).isEqualTo("REFUNDED");
        assertThat(updatedSummary.get().getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void testDeleteInvoicePaymentSummary() {
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        String transactionId = generateUniqueTransactionId();
        InvoicePaymentSummaryEntity summary = new InvoicePaymentSummaryEntity(
                null, invoice.getId(), 10l, LocalDateTime.now(), new BigDecimal("50.00"),
                "FAILED", "CASH", transactionId);
        InvoicePaymentSummaryEntity savedSummary = invoicePaymentSummaryRepository.save(summary);

        invoicePaymentSummaryRepository.deleteById(savedSummary.getId());
        Optional<InvoicePaymentSummaryEntity> deletedSummary = invoicePaymentSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByInvoiceId() {
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice1.getId(), 10l, LocalDateTime.now(), new BigDecimal("100.00"), "COMPLETED", "CREDIT_CARD", generateUniqueTransactionId()));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice1.getId(), 20l, LocalDateTime.now(), new BigDecimal("50.00"), "COMPLETED", "UPI", generateUniqueTransactionId()));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice2.getId(), 30l, LocalDateTime.now(), new BigDecimal("75.00"), "PENDING", "NET_BANKING", generateUniqueTransactionId()));

        List<InvoicePaymentSummaryEntity> paymentsForInvoice = invoicePaymentSummaryRepository.findByInvoiceId(invoice1.getId());
        assertThat(paymentsForInvoice).hasSize(2);
        assertThat(paymentsForInvoice.stream().allMatch(s -> s.getInvoiceId().equals(invoice1.getId()))).isTrue();
    }

    @Test
    void testFindByPaymentId() {
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice1.getId(), 10l, LocalDateTime.now(), new BigDecimal("80.00"), "COMPLETED", "DEBIT_CARD", generateUniqueTransactionId()));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice2.getId(), 20l, LocalDateTime.now(), new BigDecimal("90.00"), "PENDING", "WALLET", generateUniqueTransactionId()));

        Optional<InvoicePaymentSummaryEntity> foundSummary = invoicePaymentSummaryRepository.findByPaymentId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getAmount()).isEqualByComparingTo("80.00");
    }

    @Test
    void testFindByStatus() {
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice3 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE125", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice1.getId(), 10l, LocalDateTime.now(), new BigDecimal("25.00"), "COMPLETED", "CASH", generateUniqueTransactionId()));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice2.getId(), 20l, LocalDateTime.now(), new BigDecimal("35.00"), "COMPLETED", "CREDIT_CARD", generateUniqueTransactionId()));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice3.getId(), 30l, LocalDateTime.now(), new BigDecimal("45.00"), "PENDING", "UPI", generateUniqueTransactionId()));

        List<InvoicePaymentSummaryEntity> completedSummaries = invoicePaymentSummaryRepository.findByStatus("COMPLETED");
        assertThat(completedSummaries).hasSize(2);
        assertThat(completedSummaries.stream().allMatch(s -> s.getStatus().equals("COMPLETED"))).isTrue();
    }

    @Test
    void testFindByMethod() {
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice3 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE125", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));

        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice1.getId(), 10l, LocalDateTime.now(), new BigDecimal("55.00"), "COMPLETED", "NET_BANKING", generateUniqueTransactionId()));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice2.getId(), 20l, LocalDateTime.now(), new BigDecimal("65.00"), "PENDING", "NET_BANKING", generateUniqueTransactionId()));
        invoicePaymentSummaryRepository.save(new InvoicePaymentSummaryEntity(null, invoice3.getId(), 30l, LocalDateTime.now(), new BigDecimal("75.00"), "COMPLETED", "CASH", generateUniqueTransactionId()));

        List<InvoicePaymentSummaryEntity> netBankingSummaries = invoicePaymentSummaryRepository.findByMethod("NET_BANKING");
        assertThat(netBankingSummaries).hasSize(2);
        assertThat(netBankingSummaries.stream().allMatch(s -> s.getMethod().equals("NET_BANKING"))).isTrue();
    }
}
