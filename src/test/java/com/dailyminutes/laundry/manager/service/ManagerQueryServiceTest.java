package com.dailyminutes.laundry.manager.service;


import com.dailyminutes.laundry.manager.domain.model.ManagerEntity;
import com.dailyminutes.laundry.manager.repository.ManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * The type Manager query service test.
 */
@ExtendWith(MockitoExtension.class)
class ManagerQueryServiceTest {

    @Mock
    private ManagerRepository managerRepository;
    @InjectMocks
    private ManagerQueryService managerQueryService;

    /**
     * Find manager by id should return manager.
     */
    @Test
    void findManagerById_shouldReturnManager() {
        ManagerEntity manager = new ManagerEntity(1L, "Test Manager", "test@contact.com");
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        assertThat(managerQueryService.findManagerById(1L)).isPresent();
    }
}
