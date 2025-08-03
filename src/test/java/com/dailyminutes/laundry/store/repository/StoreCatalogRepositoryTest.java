package com.dailyminutes.laundry.store.repository;


import com.dailyminutes.DailyminutesApplication;
import com.dailyminutes.laundry.store.domain.model.StoreCatalogEntity;
import com.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Store catalog repository test.
 */
@DataJdbcTest(excludeAutoConfiguration = DailyminutesApplication.class)
@AutoConfigureTestDatabase(replace = Replace.ANY)
@EnableJdbcRepositories(basePackages = {"com.dailyminutes.laundry.store.repository"})
@ComponentScan(basePackages = {"com.dailyminutes.laundry.store.domain.model"})
class StoreCatalogRepositoryTest { // Updated class name

    @Autowired
    private StoreCatalogRepository storeCatalogRepository;

    @Autowired
    private StoreRepository storeRepository;

    /**
     * Test save and find store catalog.
     */
    @Test
    void testSaveAndFindStoreCatalog() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreCatalogEntity storeCatalog = new StoreCatalogEntity(null, store.getId(), 10l);
        StoreCatalogEntity savedStoreCatalog = storeCatalogRepository.save(storeCatalog);

        assertThat(savedStoreCatalog).isNotNull();
        assertThat(savedStoreCatalog.getId()).isNotNull();
        assertThat(savedStoreCatalog.getStoreId()).isEqualTo(store.getId());
        assertThat(savedStoreCatalog.getCatalogId()).isEqualTo(10l);

        Optional<StoreCatalogEntity> foundSupport = storeCatalogRepository.findById(savedStoreCatalog.getId());
        assertThat(foundSupport).isPresent();
        assertThat(foundSupport.get().getStoreId()).isEqualTo(store.getId());
        assertThat(foundSupport.get().getCatalogId()).isEqualTo(10l);
    }

    /**
     * Test update store catalog.
     */
    @Test
    void testUpdateStoreCatalog() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreCatalogEntity storeCatalog = new StoreCatalogEntity(null, store.getId(), 10l);
        StoreCatalogEntity savedStoreCatalog = storeCatalogRepository.save(storeCatalog);

        assertThat(savedStoreCatalog.getStoreId()).isEqualTo(store.getId());
        assertThat(savedStoreCatalog.getCatalogId()).isEqualTo(10l);

        Optional<StoreCatalogEntity> foundUpdatedSupport = storeCatalogRepository.findById(savedStoreCatalog.getId());

        foundUpdatedSupport.get().setCatalogId(10l);
        savedStoreCatalog = foundUpdatedSupport.get();
        StoreCatalogEntity updatedSupport = storeCatalogRepository.save(savedStoreCatalog);

        foundUpdatedSupport = storeCatalogRepository.findById(savedStoreCatalog.getId());
        assertThat(foundUpdatedSupport).isPresent();
        assertThat(foundUpdatedSupport.get().getStoreId()).isEqualTo(store.getId());
        assertThat(foundUpdatedSupport.get().getCatalogId()).isEqualTo(10l);
    }

    /**
     * Test delete store catalog.
     */
    @Test
    void testDeleteStoreCatalog() {
        StoreEntity store = storeRepository.save(new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreCatalogEntity storeCatalog = new StoreCatalogEntity(null, store.getId(), 10l);
        StoreCatalogEntity savedStoreCatalog = storeCatalogRepository.save(storeCatalog);

        storeCatalogRepository.deleteById(savedStoreCatalog.getId());
        Optional<StoreCatalogEntity> deletedSupport = storeCatalogRepository.findById(savedStoreCatalog.getId());
        assertThat(deletedSupport).isNotPresent();
    }

    /**
     * Test find by store id.
     */
    @Test
    void testFindByStoreId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", 10l));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), 10l));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), 20l));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store2.getId(), 30l));

        List<StoreCatalogEntity> storeCatalogs = storeCatalogRepository.findByStoreId(store1.getId());
        assertThat(storeCatalogs).hasSize(2);
        assertThat(storeCatalogs.stream().allMatch(s -> s.getStoreId().equals(store1.getId()))).isTrue();
    }

    /**
     * Test find by catalog id.
     */
    @Test
    void testFindByCatalogId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store1", "123 Main St", "123-456-7890", "test@example.com", 10l));
        StoreEntity store2 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", 10l));

        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), 10l));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store2.getId(), 10l));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), 20l));

        List<StoreCatalogEntity> storeCatalogs = storeCatalogRepository.findByCatalogId(10l);
        assertThat(storeCatalogs).hasSize(2);
        assertThat(storeCatalogs.stream().allMatch(s -> s.getCatalogId().equals(10l))).isTrue();
    }

    /**
     * Test find by store id and catalog id.
     */
    @Test
    void testFindByStoreIdAndCatalogId() {
        StoreEntity store1 = storeRepository.save(new StoreEntity(null, "Test Store2", "123 Main St", "123-456-7890", "test@example.com", 10l));

        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), 10l));
        storeCatalogRepository.save(new StoreCatalogEntity(null, store1.getId(), 20l));

        Optional<StoreCatalogEntity> found = storeCatalogRepository.findByStoreIdAndCatalogId(store1.getId(), 10l);
        assertThat(found).isPresent();
        assertThat(found.get().getStoreId()).isEqualTo(store1.getId());
        assertThat(found.get().getCatalogId()).isEqualTo(10l);

        assertThat(storeCatalogRepository.findByStoreIdAndCatalogId(8L, 999L)).isNotPresent();
    }
}
