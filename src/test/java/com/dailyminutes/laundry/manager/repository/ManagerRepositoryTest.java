package com.dailyminutes.laundry.manager.repository;

import com.dailyminutes.laundry.manager.domain.model.ManagerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Manager repository test.
 */
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EnableJdbcRepositories(basePackages = "com.dailyminutes.laundry.manager.repository") // Specify repository package
@ComponentScan(basePackages = "com.dailyminutes.laundry.manager.domain.model") // Specify domain model package
class ManagerRepositoryTest {

    @Autowired
    private ManagerRepository managerRepository;

    /**
     * Test save and find manager.
     */
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
