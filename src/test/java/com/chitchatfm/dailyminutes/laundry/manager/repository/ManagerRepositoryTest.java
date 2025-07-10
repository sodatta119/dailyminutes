package com.chitchatfm.dailyminutes.laundry.manager.repository;

import com.chitchatfm.dailyminutes.laundry.manager.domain.model.ManagerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ManagerRepositoryTest {

    @Autowired
    private ManagerRepository managerRepository;

    @Test
    void testSaveAndFindManager() {
        ManagerEntity manager = new ManagerEntity(null, "John Doe", "john.doe@example.com");
        ManagerEntity savedManager = managerRepository.save(manager);

        assertThat(savedManager).isNotNull();
        assertThat(savedManager.getId()).isNotNull();

        Optional<ManagerEntity> foundManager = managerRepository.findById(savedManager.getId());
        assertThat(foundManager).isPresent();
        assertThat(foundManager.get().getName()).isEqualTo("John Doe");
    }
}
