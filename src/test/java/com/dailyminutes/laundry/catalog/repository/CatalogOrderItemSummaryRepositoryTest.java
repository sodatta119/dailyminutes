package com.dailyminutes.laundry.catalog.repository;


import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogOrderItemSummaryEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;
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
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.catalog.repository") // Updated package name
@ComponentScan(basePackages = "com.dailyminutes.laundry.catalog.domain.model") // Updated package name
class CatalogOrderItemSummaryRepositoryTest {

    @Autowired
    private CatalogOrderItemSummaryRepository catalogOrderItemSummaryRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    private String generateUniqueCatalogName() {
        return "Item-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    void testSaveAndFindCatalogOrderItemSummary() {
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100)));
        String catalogName = generateUniqueCatalogName();

        CatalogOrderItemSummaryEntity summary = new CatalogOrderItemSummaryEntity(
                null, catalog.getId(), catalogName, "SERVICE", "KG", 10l, 10l,
                2, new BigDecimal("1.50"), LocalDateTime.now());
        CatalogOrderItemSummaryEntity savedSummary = catalogOrderItemSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getCatalogId()).isEqualTo(catalog.getId());
        assertThat(savedSummary.getOrderId()).isEqualTo(10l);
        assertThat(savedSummary.getOrderItemId()).isEqualTo(10l);
        assertThat(savedSummary.getCatalogName()).isEqualTo(catalogName);
        assertThat(savedSummary.getQuantity()).isEqualTo(2);
        assertThat(savedSummary.getItemPriceAtOrder()).isEqualByComparingTo("1.50");

        Optional<CatalogOrderItemSummaryEntity> foundSummary = catalogOrderItemSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getCatalogName()).isEqualTo(catalogName);
    }

    @Test
    void testUpdateCatalogOrderItemSummary() {
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100)));
        String catalogName = generateUniqueCatalogName();

        CatalogOrderItemSummaryEntity summary = new CatalogOrderItemSummaryEntity(
                null, catalog.getId(), catalogName, "PRODUCT", "PIECES", 10l, 10l,
                1, new BigDecimal("10.00"), LocalDateTime.now());
        CatalogOrderItemSummaryEntity savedSummary = catalogOrderItemSummaryRepository.save(summary);

        savedSummary.setQuantity(3);
        savedSummary.setItemPriceAtOrder(new BigDecimal("9.50"));
        catalogOrderItemSummaryRepository.save(savedSummary);

        Optional<CatalogOrderItemSummaryEntity> updatedSummary = catalogOrderItemSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getQuantity()).isEqualTo(3);
        assertThat(updatedSummary.get().getItemPriceAtOrder()).isEqualByComparingTo("9.50");
    }

    @Test
    void testDeleteCatalogOrderItemSummary() {
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100)));
        String catalogName = generateUniqueCatalogName();

        CatalogOrderItemSummaryEntity summary = new CatalogOrderItemSummaryEntity(
                null, catalog.getId(), catalogName, "SERVICE", "PIECES", 10l, 10l,
                1, new BigDecimal("5.00"), LocalDateTime.now());
        CatalogOrderItemSummaryEntity savedSummary = catalogOrderItemSummaryRepository.save(summary);

        catalogOrderItemSummaryRepository.deleteById(savedSummary.getId());
        Optional<CatalogOrderItemSummaryEntity> deletedSummary = catalogOrderItemSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByCatalogId() {
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100)));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning Premium", UnitType.KG, new BigDecimal(100)));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", 10l, 10l, 1, new BigDecimal("2.00"), LocalDateTime.now()));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", 20l, 20l, 2, new BigDecimal("3.00"), LocalDateTime.now()));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", 30l, 30l, 1, new BigDecimal("15.00"), LocalDateTime.now()));

        List<CatalogOrderItemSummaryEntity> summaries = catalogOrderItemSummaryRepository.findByCatalogId(catalog1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getCatalogId().equals(catalog1.getId()))).isTrue();
    }

    @Test
    void testFindByOrderId() {
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100)));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning Premium", UnitType.KG, new BigDecimal(100)));
        CatalogEntity catalog3 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning Regular", UnitType.KG, new BigDecimal(100)));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", 10l, 10l, 1, new BigDecimal("2.50"), LocalDateTime.now()));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", 10l, 20l, 2, new BigDecimal("12.00"), LocalDateTime.now()));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog3.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", 30l, 30l, 1, new BigDecimal("4.00"), LocalDateTime.now()));

        List<CatalogOrderItemSummaryEntity> summaries = catalogOrderItemSummaryRepository.findByOrderId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getOrderId().equals(10l))).isTrue();
    }

    @Test
    void testFindByOrderItemId() {
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning Premium", UnitType.KG, new BigDecimal(100)));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog.getId(), generateUniqueCatalogName(), "SERVICE", "KG", 10l, 15l, 1, new BigDecimal("1.75"), LocalDateTime.now()));

        Optional<CatalogOrderItemSummaryEntity> foundSummary = catalogOrderItemSummaryRepository.findByOrderItemId(15l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getItemPriceAtOrder()).isEqualByComparingTo("1.75");
    }

    @Test
    void testFindByCatalogType() {
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100)));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning Premium", UnitType.KG, new BigDecimal(100)));
        CatalogEntity catalog3 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning Regular", UnitType.KG, new BigDecimal(100)));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", 10l, 10l, 1, new BigDecimal("1.00"), LocalDateTime.now()));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", 20l, 20l, 2, new BigDecimal("2.00"), LocalDateTime.now()));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog3.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", 30l, 30l, 1, new BigDecimal("10.00"), LocalDateTime.now()));

        List<CatalogOrderItemSummaryEntity> serviceItems = catalogOrderItemSummaryRepository.findByCatalogType("SERVICE");
        assertThat(serviceItems).hasSize(2);
        assertThat(serviceItems.stream().allMatch(s -> s.getCatalogType().equals("SERVICE"))).isTrue();
    }

    @Test
    void testFindByOrderDateBetween() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 31, 23, 59);

        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100)));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning Premium", UnitType.KG, new BigDecimal(100)));
        CatalogEntity catalog3 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning Regular", UnitType.KG, new BigDecimal(100)));

        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", 10l, 10l, 1, new BigDecimal("1.00"), LocalDateTime.of(2025, 1, 15, 10, 0)));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", 20l, 20l, 1, new BigDecimal("10.00"), LocalDateTime.of(2025, 1, 20, 11, 0)));
        catalogOrderItemSummaryRepository.save(new CatalogOrderItemSummaryEntity(null, catalog3.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", 30l, 30l, 1, new BigDecimal("2.00"), LocalDateTime.of(2025, 2, 5, 12, 0))); // Outside range

        List<CatalogOrderItemSummaryEntity> itemsInDateRange = catalogOrderItemSummaryRepository.findByOrderDateBetween(start, end);
        assertThat(itemsInDateRange).hasSize(2);
        assertThat(itemsInDateRange.stream().map(CatalogOrderItemSummaryEntity::getCatalogName))
                .containsExactlyInAnyOrder(
                        catalogOrderItemSummaryRepository.findById(itemsInDateRange.get(0).getId()).get().getCatalogName(),
                        catalogOrderItemSummaryRepository.findById(itemsInDateRange.get(1).getId()).get().getCatalogName()
                );
    }
}
