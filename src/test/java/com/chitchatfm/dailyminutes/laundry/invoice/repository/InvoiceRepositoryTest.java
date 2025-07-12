package com.chitchatfm.dailyminutes.laundry.invoice.repository;

import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.UnitType;
import com.chitchatfm.dailyminutes.laundry.catalog.repository.CatalogRepository;
import com.chitchatfm.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.chitchatfm.dailyminutes.laundry.customer.repository.CustomerRepository;
import com.chitchatfm.dailyminutes.laundry.invoice.domain.model.InvoiceEntity;
import com.chitchatfm.dailyminutes.laundry.invoice.domain.model.InvoiceItemEntity;
import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest; // Changed from DataJpaTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.chitchatfm.dailyminutes.laundry.invoice.repository", "com.chitchatfm.dailyminutes.laundry.catalog.repository", "com.chitchatfm.dailyminutes.laundry.customer.repository"})
// Specify repository package
@ComponentScan(basePackages = {"com.chitchatfm.dailyminutes.laundry.invoice.domain.model", "com.chitchatfm.dailyminutes.laundry.catalog.domain.model", "com.chitchatfm.dailyminutes.laundry.customer.domain.model"}) // Specify domain model package
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    private CatalogEntity catalog1;
    private CatalogEntity catalog2;
    private CustomerEntity customer;

    @BeforeEach
    void setup(){
    this.customer=customerRepository.save(new CustomerEntity(null, "SUB123", "9876543210", "Jane Doe", "jane@example.com", "101 Elm St, Springfield, IL", "GEOFENCE_HOME_1", "12.345", "67.890"));
        this.catalog1=catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Iron", UnitType.KG, new BigDecimal("1.50")));
        this.catalog2=catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("2.20")));
    }

    @Test
    void testSaveAndFindInvoiceWithItems() {
        // 1. Save the parent InvoiceEntity
        InvoiceEntity invoice = new InvoiceEntity(null, "SWIPE123", customer.getId(), LocalDateTime.now(), new BigDecimal("50.00"), new BigDecimal("5.00"), new BigDecimal("2.00"));
        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        assertThat(savedInvoice).isNotNull();
        assertThat(savedInvoice.getId()).isNotNull();

        // 2. Save InvoiceItemEntities, linking them to the saved Invoice's ID
        InvoiceItemEntity item1 = new InvoiceItemEntity(null, savedInvoice.getId(), catalog1.getId(), 1, new BigDecimal("25.00"), new BigDecimal("2.50"));
        InvoiceItemEntity item2 = new InvoiceItemEntity(null, savedInvoice.getId(), catalog2.getId(), 2, new BigDecimal("10.00"), new BigDecimal("1.00"));
        invoiceItemRepository.save(item1);
        invoiceItemRepository.save(item2);

        // Verify the invoice itself
        Optional<InvoiceEntity> foundInvoice = invoiceRepository.findById(savedInvoice.getId());
        assertThat(foundInvoice).isPresent();
        assertThat(foundInvoice.get().getSwipeInvoiceId()).isEqualTo("SWIPE123");

        // Verify associated invoice items
        List<InvoiceItemEntity> foundItems = invoiceItemRepository.findByInvoiceId(savedInvoice.getId());
        assertThat(foundItems).hasSize(2);
        assertThat(foundItems.stream().map(InvoiceItemEntity::getCatalogId)).containsExactlyInAnyOrder(catalog1.getId(), catalog2.getId());
    }

    @Test
    void testFindBySwipeInvoiceId() {
        invoiceRepository.save(new InvoiceEntity(null, "SWIPE456", customer.getId(), LocalDateTime.now(), new BigDecimal("100.00"), new BigDecimal("10.00"), new BigDecimal("5.00")));
        Optional<InvoiceEntity> foundInvoice = invoiceRepository.findBySwipeInvoiceId("SWIPE456");
        assertThat(foundInvoice).isPresent();
        assertThat(foundInvoice.get().getCustomerId()).isEqualTo(customer.getId());
    }

    @Test
    void testUpdateInvoice() {
        InvoiceEntity invoice = new InvoiceEntity(null, "UPDATE789", customer.getId(), LocalDateTime.now(), new BigDecimal("70.00"), new BigDecimal("7.00"), new BigDecimal("3.00"));
        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        savedInvoice.setTotalPrice(new BigDecimal("75.00"));
        savedInvoice.setTotalTax(new BigDecimal("7.50"));
        invoiceRepository.save(savedInvoice);

        Optional<InvoiceEntity> updatedInvoice = invoiceRepository.findById(savedInvoice.getId());
        assertThat(updatedInvoice).isPresent();
        assertThat(updatedInvoice.get().getTotalPrice()).isEqualByComparingTo("75.00");
        assertThat(updatedInvoice.get().getTotalTax()).isEqualByComparingTo("7.50");
    }

    @Test
    void testDeleteInvoiceAndAssociatedItems() {
        // Create and save Invoice
        InvoiceEntity invoice = new InvoiceEntity(null, "DELETE000", customer.getId(), LocalDateTime.now(), new BigDecimal("20.00"), new BigDecimal("2.00"), new BigDecimal("1.00"));
        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        // Create and save InvoiceItem
        InvoiceItemEntity item1 = new InvoiceItemEntity(null, savedInvoice.getId(), catalog1.getId(), 1, new BigDecimal("20.00"), new BigDecimal("2.00"));
        invoiceItemRepository.save(item1);

        // Verify they exist
        assertThat(invoiceRepository.findById(savedInvoice.getId())).isPresent();
        assertThat(invoiceItemRepository.findByInvoiceId(savedInvoice.getId())).hasSize(1);

        // Delete the parent Invoice
        invoiceRepository.deleteById(savedInvoice.getId());

        // Verify Invoice is gone
        assertThat(invoiceRepository.findById(savedInvoice.getId())).isNotPresent();

        // Verify associated InvoiceItems are gone (assuming database CASCADE DELETE or manual cleanup in service)
        // Spring Data JDBC does NOT automatically cascade deletes for child entities.
        // You would typically handle this in your service layer or via database foreign key constraints.
        assertThat(invoiceItemRepository.findByInvoiceId(savedInvoice.getId())).isEmpty();
    }
}
