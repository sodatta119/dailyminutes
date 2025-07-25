package com.dailyminutes.laundry.manager.service;


import com.dailyminutes.laundry.manager.domain.event.ManagerCreatedEvent;
import com.dailyminutes.laundry.manager.domain.event.ManagerDeletedEvent;
import com.dailyminutes.laundry.manager.domain.event.ManagerUpdatedEvent;
import com.dailyminutes.laundry.manager.domain.model.ManagerEntity;
import com.dailyminutes.laundry.manager.dto.CreateManagerRequest;
import com.dailyminutes.laundry.manager.dto.UpdateManagerRequest;
import com.dailyminutes.laundry.manager.repository.ManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private ManagerService managerService;

    @Test
    void createManager_shouldCreateAndPublishEvent() {
        CreateManagerRequest request = new CreateManagerRequest("Test Manager", "test@contact.com");
        ManagerEntity manager = new ManagerEntity(1L, "Test Manager", "test@contact.com");
        when(managerRepository.save(any())).thenReturn(manager);

        managerService.createManager(request);

        verify(events).publishEvent(any(ManagerCreatedEvent.class));
    }

    @Test
    void updateManager_shouldUpdateAndPublishEvent() {
        UpdateManagerRequest request = new UpdateManagerRequest(1L, "Updated Manager", "updated@contact.com");
        ManagerEntity manager = new ManagerEntity(1L, "Test Manager", "test@contact.com");
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(managerRepository.save(any())).thenReturn(manager);

        managerService.updateManager(request);

        verify(events).publishEvent(any(ManagerUpdatedEvent.class));
    }

    @Test
    void deleteManager_shouldDeleteAndPublishEvent() {
        when(managerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(managerRepository).deleteById(1L);

        managerService.deleteManager(1L);

        verify(events).publishEvent(any(ManagerDeletedEvent.class));
    }

    @Test
    void updateManager_shouldThrowException_whenManagerNotFound() {
        UpdateManagerRequest request = new UpdateManagerRequest(1L, "Updated Manager", "updated@contact.com");
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> managerService.updateManager(request));
    }
}
