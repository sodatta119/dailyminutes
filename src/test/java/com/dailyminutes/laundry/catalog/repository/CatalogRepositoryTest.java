package com.dailyminutes.laundry.catalog.repository;


import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Catalog repository test.
 */
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.catalog.repository") // Specify repository package
@ComponentScan(basePackages = "com.dailyminutes.laundry.catalog.domain.model") // Specify domain model package
class CatalogRepositoryTest {

    @Autowired
    private CatalogRepository catalogRepository;

    /**
     * Test save and find catalog item.
     */
    @Test
    void testSaveAndFindCatalogItem() {
        CatalogEntity item = new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal(100));
        CatalogEntity savedItem = catalogRepository.save(item);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();

        Optional<CatalogEntity> foundItem = catalogRepository.findById(savedItem.getId());
        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getName()).isEqualTo("Wash & Fold");
    }

    /**
     * Test update catalog item.
     */
    @Test
    void testUpdateCatalogItem() {
        CatalogEntity item = new CatalogEntity(null, CatalogType.PRODUCT, "Detergent", UnitType.KG, new BigDecimal(100));
        CatalogEntity savedItem = catalogRepository.save(item);

        CatalogEntity updatedItem = catalogRepository.save(savedItem);

        Optional<CatalogEntity> foundUpdatedItem = catalogRepository.findById(updatedItem.getId());
        assertThat(foundUpdatedItem).isPresent();
    }

    /**
     * Test delete catalog item.
     */
    @Test
    void testDeleteCatalogItem() {
        CatalogEntity item = new CatalogEntity(null, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100));
        CatalogEntity savedItem = catalogRepository.save(item);

        catalogRepository.deleteById(savedItem.getId());

        Optional<CatalogEntity> deletedItem = catalogRepository.findById(savedItem.getId());
        assertThat(deletedItem).isNotPresent();
    }
}
