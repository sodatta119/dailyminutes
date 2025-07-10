package com.chitchatfm.dailyminutes.laundry.catalog.repository;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.UnitType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CatalogRepositoryTest {

    @Autowired
    private CatalogRepository catalogRepository;

    @Test
    void testSaveAndFindCatalogItem() {
        CatalogEntity item = new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("1.50"));
        CatalogEntity savedItem = catalogRepository.save(item);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();

        Optional<CatalogEntity> foundItem = catalogRepository.findById(savedItem.getId());
        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getName()).isEqualTo("Wash & Fold");
    }
}
