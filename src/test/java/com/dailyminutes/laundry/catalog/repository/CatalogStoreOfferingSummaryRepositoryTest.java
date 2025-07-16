package com.dailyminutes.laundry.catalog.repository; // Updated package name

import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogStoreOfferingSummaryEntity;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.catalog.repository")
@ComponentScan(basePackages = "com.dailyminutes.laundry.catalog.domain.model")
class CatalogStoreOfferingSummaryRepositoryTest {

    @Autowired
    private CatalogStoreOfferingSummaryRepository catalogStoreOfferingSummaryRepository;

    @Autowired
    private CatalogRepository catalogRepository;


    private String generateUniqueCatalogName() {
        return "Service-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateUniqueStoreName() {
        return "Store-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    void testSaveAndFindCatalogStoreOfferingSummary() {
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        String catalogName = generateUniqueCatalogName();
        String storeName = generateUniqueStoreName();

        CatalogStoreOfferingSummaryEntity summary = new CatalogStoreOfferingSummaryEntity(
                null, catalog.getId(), catalogName, "SERVICE", "KG", new BigDecimal("2.00"), 10l, storeName, // Added unitPrice
                new BigDecimal("1.50"), LocalDate.now(), LocalDate.now().plusYears(1), true);
        CatalogStoreOfferingSummaryEntity savedSummary = catalogStoreOfferingSummaryRepository.save(summary);

        assertThat(savedSummary).isNotNull();
        assertThat(savedSummary.getId()).isNotNull();
        assertThat(savedSummary.getCatalogId()).isEqualTo(catalog.getId());
        assertThat(savedSummary.getStoreId()).isEqualTo(10l);
        assertThat(savedSummary.getCatalogName()).isEqualTo(catalogName);
        assertThat(savedSummary.getStoreName()).isEqualTo(storeName);
        assertThat(savedSummary.isActive()).isTrue();
        assertThat(savedSummary.getUnitPrice()).isEqualByComparingTo("2.00"); // Assert unitPrice

        Optional<CatalogStoreOfferingSummaryEntity> foundSummary = catalogStoreOfferingSummaryRepository.findById(savedSummary.getId());
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreSpecificPrice()).isEqualByComparingTo("1.50");
        assertThat(foundSummary.get().getUnitPrice()).isEqualByComparingTo("2.00"); // Assert unitPrice
    }

    @Test
    void testUpdateCatalogStoreOfferingSummary() {
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        String catalogName = generateUniqueCatalogName();
        String storeName = generateUniqueStoreName();

        CatalogStoreOfferingSummaryEntity summary = new CatalogStoreOfferingSummaryEntity(
                null, catalog.getId(), catalogName, "PRODUCT", "PIECES", new BigDecimal("12.00"), 10l, storeName, // Added unitPrice
                new BigDecimal("10.00"), LocalDate.now(), LocalDate.now().plusMonths(6), true);
        CatalogStoreOfferingSummaryEntity savedSummary = catalogStoreOfferingSummaryRepository.save(summary);

        savedSummary.setStoreSpecificPrice(new BigDecimal("9.50"));
        savedSummary.setActive(false);
        savedSummary.setUnitPrice(new BigDecimal("11.50")); // Update unitPrice
        catalogStoreOfferingSummaryRepository.save(savedSummary);

        Optional<CatalogStoreOfferingSummaryEntity> updatedSummary = catalogStoreOfferingSummaryRepository.findById(savedSummary.getId());
        assertThat(updatedSummary).isPresent();
        assertThat(updatedSummary.get().getStoreSpecificPrice()).isEqualByComparingTo("9.50");
        assertThat(updatedSummary.get().isActive()).isFalse();
        assertThat(updatedSummary.get().getUnitPrice()).isEqualByComparingTo("11.50"); // Assert updated unitPrice
    }

    @Test
    void testDeleteCatalogStoreOfferingSummary() {
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        String catalogName = generateUniqueCatalogName();
        String storeName = generateUniqueStoreName();

        CatalogStoreOfferingSummaryEntity summary = new CatalogStoreOfferingSummaryEntity(
                null, catalog.getId(), catalogName, "SERVICE", "PIECES", new BigDecimal("6.00"), 10l, storeName, // Added unitPrice
                new BigDecimal("5.00"), LocalDate.now(), LocalDate.now().plusMonths(3), true);
        CatalogStoreOfferingSummaryEntity savedSummary = catalogStoreOfferingSummaryRepository.save(summary);

        catalogStoreOfferingSummaryRepository.deleteById(savedSummary.getId());
        Optional<CatalogStoreOfferingSummaryEntity> deletedSummary = catalogStoreOfferingSummaryRepository.findById(savedSummary.getId());
        assertThat(deletedSummary).isNotPresent();
    }

    @Test
    void testFindByCatalogId() {
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", new BigDecimal("2.00"), 10l, generateUniqueStoreName(), new BigDecimal("2.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", new BigDecimal("3.50"), 20l, generateUniqueStoreName(), new BigDecimal("3.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", new BigDecimal("18.00"), 30l, generateUniqueStoreName(), new BigDecimal("15.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));

        List<CatalogStoreOfferingSummaryEntity> summaries = catalogStoreOfferingSummaryRepository.findByCatalogId(catalog1.getId());
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getCatalogId().equals(catalog1.getId()))).isTrue();
    }

    @Test
    void testFindByStoreId() {
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog3 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", new BigDecimal("2.50"), 10l, generateUniqueStoreName(), new BigDecimal("2.50"), LocalDate.now(), LocalDate.now().plusYears(1), true));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", new BigDecimal("15.00"), 10l, generateUniqueStoreName(), new BigDecimal("12.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog3.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", new BigDecimal("4.50"), 20l, generateUniqueStoreName(), new BigDecimal("4.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));

        List<CatalogStoreOfferingSummaryEntity> summaries = catalogStoreOfferingSummaryRepository.findByStoreId(10l);
        assertThat(summaries).hasSize(2);
        assertThat(summaries.stream().allMatch(s -> s.getStoreId().equals(10l))).isTrue();
    }

    @Test
    void testFindByCatalogIdAndStoreId() {
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog.getId(), generateUniqueCatalogName(), "SERVICE", "KG", new BigDecimal("1.80"), 10l, generateUniqueStoreName(), new BigDecimal("1.75"), LocalDate.now(), LocalDate.now().plusYears(1), true));

        Optional<CatalogStoreOfferingSummaryEntity> foundSummary = catalogStoreOfferingSummaryRepository.findByCatalogIdAndStoreId(catalog.getId(), 10l);
        assertThat(foundSummary).isPresent();
        assertThat(foundSummary.get().getStoreSpecificPrice()).isEqualByComparingTo("1.75");
        assertThat(foundSummary.get().getUnitPrice()).isEqualByComparingTo("1.80"); // Assert unitPrice
    }

    @Test
    void testFindByCatalogType() {
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog3 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", new BigDecimal("1.00"), 10l, generateUniqueStoreName(), new BigDecimal("1.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", new BigDecimal("2.00"), 20l, generateUniqueStoreName(), new BigDecimal("2.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog3.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", new BigDecimal("10.00"), 30l, generateUniqueStoreName(), new BigDecimal("10.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));

        List<CatalogStoreOfferingSummaryEntity> serviceOfferings = catalogStoreOfferingSummaryRepository.findByCatalogType("SERVICE");
        assertThat(serviceOfferings).hasSize(2);
        assertThat(serviceOfferings.stream().allMatch(s -> s.getCatalogType().equals("SERVICE"))).isTrue();
    }

    @Test
    void testFindByActive() {
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog3 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));

        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", new BigDecimal("1.00"), 10l, generateUniqueStoreName(), new BigDecimal("1.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", new BigDecimal("10.00"), 20l, generateUniqueStoreName(), new BigDecimal("10.00"), LocalDate.now(), LocalDate.now().plusYears(1), false));
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog3.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", new BigDecimal("2.00"), 30l, generateUniqueStoreName(), new BigDecimal("2.00"), LocalDate.now(), LocalDate.now().plusYears(1), true));

        List<CatalogStoreOfferingSummaryEntity> activeOfferings = catalogStoreOfferingSummaryRepository.findByActive(true);
        assertThat(activeOfferings).hasSize(2);
        assertThat(activeOfferings.stream().allMatch(CatalogStoreOfferingSummaryEntity::isActive)).isTrue();
    }

    @Test
    void testFindByEffectiveToAfter() {
        LocalDate today = LocalDate.now();
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));
        CatalogEntity catalog3 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("5.00")));

        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog1.getId(), generateUniqueCatalogName(), "SERVICE", "KG", new BigDecimal("1.00"), 10l, generateUniqueStoreName(), new BigDecimal("1.00"), today.minusMonths(1), today.plusDays(5), true)); // Expires soon
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog2.getId(), generateUniqueCatalogName(), "PRODUCT", "PIECES", new BigDecimal("10.00"), 20l, generateUniqueStoreName(), new BigDecimal("10.00"), today.minusMonths(2), today.plusMonths(6), true)); // Expires later
        catalogStoreOfferingSummaryRepository.save(new CatalogStoreOfferingSummaryEntity(null, catalog3.getId(), generateUniqueCatalogName(), "SERVICE", "PIECES", new BigDecimal("2.00"), 30l, generateUniqueStoreName(), new BigDecimal("2.00"), today.minusMonths(3), today.minusDays(1), true)); // Already expired

        List<CatalogStoreOfferingSummaryEntity> validOfferings = catalogStoreOfferingSummaryRepository.findByEffectiveToAfter(today);
        assertThat(validOfferings).hasSize(2);
        assertThat(validOfferings.stream().allMatch(s -> s.getEffectiveTo().isAfter(today))).isTrue();
    }
}
