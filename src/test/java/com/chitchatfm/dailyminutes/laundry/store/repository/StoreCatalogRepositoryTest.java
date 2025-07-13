package com.chitchatfm.dailyminutes.laundry.store.repository;


import com.chitchatfm.dailyminutes.DailyminutesApplication;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.chitchatfm.dailyminutes.laundry.catalog.domain.model.UnitType;
import com.chitchatfm.dailyminutes.laundry.catalog.repository.CatalogRepository;
import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreCatalogEntity; // Updated import
import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = {"com.chitchatfm.dailyminutes.laundry.store.repository",
        "com.chitchatfm.dailyminutes.laundry.catalog.repository"})
@ComponentScan(basePackages = {"com.chitchatfm.dailyminutes.laundry.store.domain.model",
        "com.chitchatfm.dailyminutes.laundry.catalog.domain.model"})
class StoreCatalogRepositoryTest { // Updated class name

    @Autowired
    private StoreCatalogRepository storeCatalogRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @BeforeEach
    void setup(){

    }

    @Test
    void testSaveAndFindStoreCatalog() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("1.50")));
        StoreCatalogEntity storeCatalog = new StoreCatalogEntity(null, store.getId(), catalog.getId());
        StoreCatalogEntity savedStoreCatalog = storeCatalogRepository.save(storeCatalog);

        assertThat(savedStoreCatalog).isNotNull();
        assertThat(savedStoreCatalog.getId()).isNotNull();
        assertThat(savedStoreCatalog.getStoreId()).isEqualTo(store.getId());
        assertThat(savedStoreCatalog.getCatalogId()).isEqualTo(catalog.getId());

        Optional<StoreCatalogEntity> foundSupport = storeCatalogRepository.findById(savedStoreCatalog.getId());
        assertThat(foundSupport).isPresent();
        assertThat(foundSupport.get().getStoreId()).isEqualTo(store.getId());
        assertThat(foundSupport.get().getCatalogId()).isEqualTo(catalog.getId());
    }

    @Test
    void testUpdateStoreCatalog() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("1.50")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Iron", UnitType.KG, new BigDecimal("2.50")));
        StoreCatalogEntity storeCatalog = new StoreCatalogEntity(null, store.getId(), catalog1.getId());
        StoreCatalogEntity savedStoreCatalog = storeCatalogRepository.save(storeCatalog);

        assertThat(savedStoreCatalog.getStoreId()).isEqualTo(store.getId());
        assertThat(savedStoreCatalog.getCatalogId()).isEqualTo(catalog1.getId());

        Optional<StoreCatalogEntity> foundUpdatedSupport = storeCatalogRepository.findById(savedStoreCatalog.getId());

        foundUpdatedSupport.get().setCatalogId(catalog2.getId());
        savedStoreCatalog=foundUpdatedSupport.get();
        StoreCatalogEntity updatedSupport = storeCatalogRepository.save(savedStoreCatalog);

        foundUpdatedSupport = storeCatalogRepository.findById(savedStoreCatalog.getId());
        assertThat(foundUpdatedSupport).isPresent();
        assertThat(foundUpdatedSupport.get().getStoreId()).isEqualTo(store.getId());
        assertThat(foundUpdatedSupport.get().getCatalogId()).isEqualTo(catalog2.getId());
    }

    @Test
    void testDeleteStoreCatalog() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CatalogEntity catalog = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("1.50")));
        StoreCatalogEntity storeCatalog = new StoreCatalogEntity(null, store.getId(), catalog.getId());
        StoreCatalogEntity savedStoreCatalog = storeCatalogRepository.save(storeCatalog);

        storeCatalogRepository.deleteById(savedStoreCatalog.getId());
        Optional<StoreCatalogEntity> deletedSupport = storeCatalogRepository.findById(savedStoreCatalog.getId());
        assertThat(deletedSupport).isNotPresent();
    }

    @Test
    void testFindByStoreId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", 10L));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("1.50")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Iron", UnitType.KG, new BigDecimal("2.50")));
        CatalogEntity catalog3 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Dryclean", UnitType.KG, new BigDecimal("3.50")));

        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), catalog1.getId()));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), catalog2.getId()));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store2.getId(), catalog3.getId()));

        List<StoreCatalogEntity> storeCatalogs = storeCatalogRepository.findByStoreId(store1.getId());
        assertThat(storeCatalogs).hasSize(2);
        assertThat(storeCatalogs.stream().allMatch(s -> s.getStoreId().equals(store1.getId()))).isTrue();
    }

    @Test
    void testFindByCatalogId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", 10L));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("1.50")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Iron", UnitType.KG, new BigDecimal("2.50")));

        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), catalog1.getId()));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store2.getId(), catalog1.getId()));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), catalog2.getId()));

        List<StoreCatalogEntity> storeCatalogs = storeCatalogRepository.findByCatalogId(catalog1.getId());
        assertThat(storeCatalogs).hasSize(2);
        assertThat(storeCatalogs.stream().allMatch(s -> s.getCatalogId().equals(catalog1.getId()))).isTrue();
    }

    @Test
    void testFindByStoreIdAndCatalogId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", 10L));
        CatalogEntity catalog1 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Fold", UnitType.KG, new BigDecimal("1.50")));
        CatalogEntity catalog2 = catalogRepository.save(new CatalogEntity(null, CatalogType.SERVICE, "Wash & Iron", UnitType.KG, new BigDecimal("2.50")));

        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), catalog1.getId()));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), catalog2.getId()));

        Optional<StoreCatalogEntity> found = storeCatalogRepository.findByStoreIdAndCatalogId(store1.getId(), catalog1.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStoreId()).isEqualTo(store1.getId());
        assertThat(found.get().getCatalogId()).isEqualTo(catalog1.getId());

        assertThat(storeCatalogRepository.findByStoreIdAndCatalogId(8L, 999L)).isNotPresent();
    }
}
