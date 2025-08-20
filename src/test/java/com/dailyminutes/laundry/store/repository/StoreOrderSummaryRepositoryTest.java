package com.dailyminutes.laundry.store.repository;

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import com.dailyminutes.laundry.store.domain.model.StoreOrderSummaryEntity;
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
 * The type Store order summary repository test.
 *
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16 /07/25
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.store.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.store.domain.model") // Updated package name
class StoreOrderSummaryRepositoryTest {

    @Autowired
    private StoreOrderSummaryRepository storeOrderSummaryRepository;

    @Autowired
    private StoreRepository storeRepository;


    /**
     * Test save and find store order summary.
     */
    @Test
    void testSaveAndFindStoreOrderSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));

        StoreOrderSummaryEntity summary = new StoreOrderSummaryEntity(null, store.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("100.00"), 10l);
        StoreOrderSummaryEntity savedSummary = storeOrderSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getStoreId()).isEqualTo(store.getId());
        assertThat(savedSummary.getOrderId()).isEqualTo(10l);
        assertThat(savedSummary.getCustomerId()).isEqualTo(10l);
        assertThat(savedSummary.getStatus()).isEqualTo("PENDING");

        Optional<StoreOrderSummaryEntity> foundSummary = storeOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getTotalAmount()).isEqualByComparingTo("100.00");
    }

    /**
     * Test update store order summary.
     */
    @Test
    void testUpdateStoreOrderSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));

        StoreOrderSummaryEntity summary = new StoreOrderSummaryEntity(null, store.getId(), 10l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("150.00"), 10l);
        StoreOrderSummaryEntity savedSummary = storeOrderSummaryRepository.save(summary);

        savedSummary.setStatus("DELIVERED");
        savedSummary.setTotalAmount(new BigDecimal("145.00"));
        storeOrderSummaryRepository.save(savedSummary);

        Optional<StoreOrderSummaryEntity> updatedSummary = storeOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStatus()).isEqualTo("DELIVERED");
        assertThat(updatedSummary.get().getTotalAmount()).isEqualByComparingTo("145.00");
    }

    /**
     * Test delete store order summary.
     */
    @Test
    void testDeleteStoreOrderSummary() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));

        StoreOrderSummaryEntity summary = new StoreOrderSummaryEntity(null, store.getId(), 10l, LocalDateTime.now(), "CANCELLED", new BigDecimal("50.00"), 10l);
        StoreOrderSummaryEntity savedSummary = storeOrderSummaryRepository.save(summary);

        storeOrderSummaryRepository.deleteById(savedSummary.getId());
        Optional<StoreOrderSummaryEntity> deletedSummary = storeOrderSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    /**
     * Test find by store id.
     */
    @Test
    void testFindByStoreId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store1.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("10.00"), 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store1.getId(), 20l, LocalDateTime.now(), "DELIVERED", new BigDecimal("20.00"), 20l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store2.getId(), 30l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("30.00"), 30l));

        List<StoreOrderSummaryEntity> summaries = storeOrderSummaryRepository.findByStoreId(store1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getStoreId().equals(store1.getId()))).isTrue();
    }

    /**
     * Test find by order id.
     */
    @Test
    void testFindByOrderId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store1.getId(), 10l, LocalDateTime.now(), "READY_FOR_PICKUP", new BigDecimal("75.00"), 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store2.getId(), 20l, LocalDateTime.now(), "IN_PROCESS", new BigDecimal("85.00"), 20l));

        Optional<StoreOrderSummaryEntity> foundSummary = storeOrderSummaryRepository.findByOrderId(10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStatus()).isEqualTo("READY_FOR_PICKUP");
    }

    /**
     * Test find by customer id.
     */
    @Test
    void testFindByCustomerId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store1.getId(), 10l, LocalDateTime.now(), "PENDING", new BigDecimal("110.00"), 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store2.getId(), 20l, LocalDateTime.now(), "DELIVERED", new BigDecimal("120.00"), 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store3.getId(), 30l, LocalDateTime.now(), "ACCEPTED", new BigDecimal("130.00"), 20l));

        List<StoreOrderSummaryEntity> summaries = storeOrderSummaryRepository.findByCustomerId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getCustomerId().equals(10l))).isTrue();
    }

    /**
     * Test find by status.
     */
    @Test
    void testFindByStatus() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store3 = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store1.getId(), 10l, LocalDateTime.now(), "IN_PROCESS", new BigDecimal("25.00"), 10l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store2.getId(), 20l, LocalDateTime.now(), "IN_PROCESS", new BigDecimal("35.00"), 20l));
        storeOrderSummaryRepository.save(new StoreOrderSummaryEntity(null, store3.getId(), 30l, LocalDateTime.now(), "DELIVERED", new BigDecimal("45.00"), 30l));

        List<StoreOrderSummaryEntity> inProcessSummaries = storeOrderSummaryRepository.findByStatus("IN_PROCESS");
        assertThat(inProcessSummaries).hasSize(2);
        assertThat(inProcessSummaries.stream().allMatch(s -> s.getStatus().equals("IN_PROCESS"))).isTrue();
    }
}
