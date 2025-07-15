package com.dailyminutes.laundry.invoice.repository; // Updated package name

import com.dailyminutes.DailyminutesApplication; // Updated import
import com.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceOrderSummaryEntity; // Updated import
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
 * @version 12/07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.invoice.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.invoice.domain.model") // Updated package name
class InvoiceOrderSummaryRepositoryTest {

    @Autowired
    private InvoiceOrderSummaryRepository invoiceOrderSummaryRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;



    @Test
    void testSaveAndFindInvoiceOrderSummary() {
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceOrderSummaryEntity summary = new InvoiceOrderSummaryEntity(
                null, invoice.getId(), 10l, LocalDateTime.now(), "COMPLETED", new BigDecimal("120.50"));
        InvoiceOrderSummaryEntity savedSummary = invoiceOrderSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getInvoiceId()).isEqualTo(invoice.getId());
        assertThat(savedSummary.getOrderId()).isEqualTo(10l);
        assertThat(savedSummary.getStatus()).isEqualTo("COMPLETED");

        Optional<InvoiceOrderSummaryEntity> foundSummary = invoiceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalAmount()).isEqualByComparingTo("120.50");
    }

    @Test
    void testUpdateInvoiceOrderSummary() {
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceOrderSummaryEntity summary = new InvoiceOrderSummaryEntity(
                null, invoice.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("80.00"));
        InvoiceOrderSummaryEntity savedSummary = invoiceOrderSummaryRepository.save(summary);

        savedSummary.setStatus("DELIVERED");
        savedSummary.setTotalAmount(new BigDecimal("85.00"));
        invoiceOrderSummaryRepository.save(savedSummary);

        Optional<InvoiceOrderSummaryEntity> updatedSummary = invoiceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStatus()).isEqualTo("DELIVERED");
        assertThat(updatedSummary.get().getTotalAmount()).isEqualByComparingTo("85.00");
    }

    @Test
    void testDeleteInvoiceOrderSummary() {
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceOrderSummaryEntity summary = new InvoiceOrderSummaryEntity(
                null, invoice.getId(), 10l, LocalDateTime.now(), "CANCELLED", new BigDecimal("30.00"));
        InvoiceOrderSummaryEntity savedSummary = invoiceOrderSummaryRepository.save(summary);

        invoiceOrderSummaryRepository.deleteById(savedSummary.getId());
        Optional<InvoiceOrderSummaryEntity> deletedSummary = invoiceOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByInvoiceId() {
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        invoiceOrderSummaryRepository.save(new InvoiceOrderSummaryEntity(null, invoice1.getId(), 10l, LocalDateTime.now(), "COMPLETED", new BigDecimal("100.00")));
        invoiceOrderSummaryRepository.save(new InvoiceOrderSummaryEntity(null, invoice2.getId(), 20l, LocalDateTime.now(), "PENDING", new BigDecimal("50.00")));

        Optional<InvoiceOrderSummaryEntity> foundSummary = invoiceOrderSummaryRepository.findByInvoiceId(invoice1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void testFindByOrderId() {
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        invoiceOrderSummaryRepository.save(new InvoiceOrderSummaryEntity(null, invoice1.getId(), 10l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("70.00")));
        invoiceOrderSummaryRepository.save(new InvoiceOrderSummaryEntity(null, invoice2.getId(), 20l, LocalDateTime.now(), "DELIVERED", new BigDecimal("90.00")));

        Optional<InvoiceOrderSummaryEntity> foundSummary = invoiceOrderSummaryRepository.findByOrderId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("ACCEPTED");
    }

    @Test
    void testFindByStatus() {
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice3 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE125", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        invoiceOrderSummaryRepository.save(new InvoiceOrderSummaryEntity(null, invoice1.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("25.00")));
        invoiceOrderSummaryRepository.save(new InvoiceOrderSummaryEntity(null, invoice2.getId(), 20l, LocalDateTime.now(), "PENDING", new BigDecimal("35.00")));
        invoiceOrderSummaryRepository.save(new InvoiceOrderSummaryEntity(null, invoice3.getId(), 30l, LocalDateTime.now(), "DELIVERED", new BigDecimal("45.00")));

        List<InvoiceOrderSummaryEntity> pendingSummaries = invoiceOrderSummaryRepository.findByStatus("PENDING");
        assertThat(pendingSummaries).hasSize(2);
        assertThat(pendingSummaries.stream().allMatch(s -> s.getStatus().equals("PENDING"))).isTrue();
    }
}
