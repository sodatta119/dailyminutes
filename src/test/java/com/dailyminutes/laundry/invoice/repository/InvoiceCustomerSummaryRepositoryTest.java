/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.invoice.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceCustomerSummaryEntity;
import com.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
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

@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.invoice.repository")
@ComponentScan(basePackages = "com.dailyminutes.laundry.invoice.domain.model")
class InvoiceCustomerSummaryRepositoryTest {

    @Autowired
    private InvoiceCustomerSummaryRepository invoiceCustomerSummaryRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    // Helper to generate unique phone numbers for test isolation
    private String generateUniquePhoneNumber() {
        return "9" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
    }

    // Helper to generate unique emails for test isolation
    private String generateUniqueEmail() {
        return "test_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    @Test
    void testSaveAndFindInvoiceCustomerSummary() {
        // Build entity locally
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceCustomerSummaryEntity summary = new InvoiceCustomerSummaryEntity(
                null, invoice.getId(), 10L, "John Doe", generateUniquePhoneNumber(), generateUniqueEmail());
        InvoiceCustomerSummaryEntity savedSummary = invoiceCustomerSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getInvoiceId()).isEqualTo(invoice.getId());
        assertThat(savedSummary.getCustomerId()).isEqualTo(10L);
        assertThat(savedSummary.getCustomerName()).isEqualTo("John Doe");

        Optional<InvoiceCustomerSummaryEntity> foundSummary = invoiceCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerEmail()).isEqualTo(summary.getCustomerEmail()); // Use original email for assertion
    }

    @Test
    void testUpdateInvoiceCustomerSummary() {
        // Build entity locally
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceCustomerSummaryEntity summary = new InvoiceCustomerSummaryEntity(
                null, invoice.getId(), 11L, "Jane Smith", generateUniquePhoneNumber(), generateUniqueEmail());
        InvoiceCustomerSummaryEntity savedSummary = invoiceCustomerSummaryRepository.save(summary);

        savedSummary.setCustomerName("Jane A. Smith");
        savedSummary.setCustomerEmail(generateUniqueEmail()); // Update with a new unique email
        invoiceCustomerSummaryRepository.save(savedSummary);

        Optional<InvoiceCustomerSummaryEntity> updatedSummary = invoiceCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getCustomerName()).isEqualTo("Jane A. Smith");
        assertThat(updatedSummary.get().getCustomerEmail()).isEqualTo(savedSummary.getCustomerEmail()); // Use updated email for assertion
    }

    @Test
    void testDeleteInvoiceCustomerSummary() {
        // Build entity locally
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceCustomerSummaryEntity summary = new InvoiceCustomerSummaryEntity(
                null, invoice.getId(), 12L, "Customer to Delete", generateUniquePhoneNumber(), generateUniqueEmail());
        InvoiceCustomerSummaryEntity savedSummary = invoiceCustomerSummaryRepository.save(summary);

        invoiceCustomerSummaryRepository.deleteById(savedSummary.getId());
        Optional<InvoiceCustomerSummaryEntity> deletedSummary = invoiceCustomerSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByInvoiceId() {
        // Build entities locally
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceCustomerSummaryEntity summary1 = new InvoiceCustomerSummaryEntity(null, invoice1.getId(), 20L, "Inv Cust A", generateUniquePhoneNumber(), generateUniqueEmail());
        InvoiceCustomerSummaryEntity summary2 = new InvoiceCustomerSummaryEntity(null, invoice2.getId(), 21L, "Inv Cust B", generateUniquePhoneNumber(), generateUniqueEmail());
        invoiceCustomerSummaryRepository.save(summary1);
        invoiceCustomerSummaryRepository.save(summary2);

        Optional<InvoiceCustomerSummaryEntity> foundSummary = invoiceCustomerSummaryRepository.findByInvoiceId(invoice1.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Inv Cust A");
        assertThat(foundSummary.get().getCustomerId()).isEqualTo(20L);
    }

    @Test
    void testFindByCustomerId() {
        // Build entities locally
        InvoiceEntity invoice1 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice2 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE124", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceEntity invoice3 = invoiceRepository.save(new InvoiceEntity(null, "SWIPE125", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        InvoiceCustomerSummaryEntity summary3 = new InvoiceCustomerSummaryEntity(null, invoice1.getId(), 30L, "Cust C", generateUniquePhoneNumber(), generateUniqueEmail());
        InvoiceCustomerSummaryEntity summary4 = new InvoiceCustomerSummaryEntity(null, invoice2.getId(), 30L, "Cust D", generateUniquePhoneNumber(), generateUniqueEmail()); // Same customer, different invoice
        InvoiceCustomerSummaryEntity summary5 = new InvoiceCustomerSummaryEntity(null, invoice3.getId(), 31L, "Cust E", generateUniquePhoneNumber(), generateUniqueEmail());
        invoiceCustomerSummaryRepository.save(summary3);
        invoiceCustomerSummaryRepository.save(summary4);
        invoiceCustomerSummaryRepository.save(summary5);

        List<InvoiceCustomerSummaryEntity> summariesForCustomer30 = invoiceCustomerSummaryRepository.findByCustomerId(30L);
        assertThat(summariesForCustomer30).hasSize(2);
        assertThat(summariesForCustomer30.stream().allMatch(s -> s.getCustomerId().equals(30L))).isTrue();
    }

    @Test
    void testFindByCustomerPhoneNumber() {
        // Build entity locally
        InvoiceEntity invoice = invoiceRepository.save(new InvoiceEntity(null, "SWIPE123", 10l, LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00")));
        String phoneNumber = generateUniquePhoneNumber();
        InvoiceCustomerSummaryEntity summary6 = new InvoiceCustomerSummaryEntity(null, invoice.getId(), 40L, "Cust F", phoneNumber, generateUniqueEmail());
        invoiceCustomerSummaryRepository.save(summary6);

        Optional<InvoiceCustomerSummaryEntity> foundSummary = invoiceCustomerSummaryRepository.findByCustomerPhoneNumber(phoneNumber);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCustomerName()).isEqualTo("Cust F");
    }
}
