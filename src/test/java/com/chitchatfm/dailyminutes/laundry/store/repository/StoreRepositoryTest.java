package com.chitchatfm.dailyminutes.laundry.store.repository;


import com.chitchatfm.dailyminutes.laundry.store.domain.model.StoreEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void testSaveAndFindStore() {
        StoreEntity store = new StoreEntity(null, "Test Store", "123 Main St", "123-456-7890", "test@example.com", null);
        StoreEntity savedStore = storeRepository.save(store);

        assertThat(savedStore).isNotNull();
        assertThat(savedStore.getId()).isNotNull();

        Optional<StoreEntity> foundStore = storeRepository.findById(savedStore.getId());
        assertThat(foundStore).isPresent();
        assertThat(foundStore.get().getName()).isEqualTo("Test Store");
    }

    @Test
    void testFindAllStores() {
        storeRepository.save(new StoreEntity(null, "Store A", "Address A", "111", "a@example.com", null));
        storeRepository.save(new StoreEntity(null, "Store B", "Address B", "222", "b@example.com", null));

        List<StoreEntity> stores = storeRepository.findAll();
        assertThat(stores).hasSize(2);
    }

    @Test
    void testDeleteStore() {
        StoreEntity store = new StoreEntity(null, "Store to Delete", "Delete Address", "333", "delete@example.com", null);
        StoreEntity savedStore = storeRepository.save(store);

        storeRepository.deleteById(savedStore.getId());
        Optional<StoreEntity> foundStore = storeRepository.findById(savedStore.getId());
        assertThat(foundStore).isNotPresent();
    }
}
