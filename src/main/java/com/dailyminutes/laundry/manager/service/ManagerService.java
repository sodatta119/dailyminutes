/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager.service;


import com.dailyminutes.laundry.manager.domain.event.ManagerCreatedEvent;
import com.dailyminutes.laundry.manager.domain.event.ManagerDeletedEvent;
import com.dailyminutes.laundry.manager.domain.event.ManagerUpdatedEvent;
import com.dailyminutes.laundry.manager.domain.model.ManagerEntity;
import com.dailyminutes.laundry.manager.dto.CreateManagerRequest;
import com.dailyminutes.laundry.manager.dto.ManagerResponse;
import com.dailyminutes.laundry.manager.dto.UpdateManagerRequest;
import com.dailyminutes.laundry.manager.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final ApplicationEventPublisher events;

    public ManagerResponse createManager(CreateManagerRequest request) {
        ManagerEntity manager = new ManagerEntity(null, request.name(), request.contact());
        ManagerEntity savedManager = managerRepository.save(manager);
        events.publishEvent(new ManagerCreatedEvent(savedManager.getId(), savedManager.getName()));
        return toManagerResponse(savedManager);
    }

    public ManagerResponse updateManager(UpdateManagerRequest request) {
        ManagerEntity existingManager = managerRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Manager with ID " + request.id() + " not found."));

        existingManager.setName(request.name());
        existingManager.setContact(request.contact());

        ManagerEntity updatedManager = managerRepository.save(existingManager);
        events.publishEvent(new ManagerUpdatedEvent(updatedManager.getId(), updatedManager.getName(), updatedManager.getContact()));
        return toManagerResponse(updatedManager);
    }

    public void deleteManager(Long id) {
        if (!managerRepository.existsById(id)) {
            throw new IllegalArgumentException("Manager with ID " + id + " not found.");
        }
        managerRepository.deleteById(id);
        events.publishEvent(new ManagerDeletedEvent(id));
    }

    private ManagerResponse toManagerResponse(ManagerEntity entity) {
        return new ManagerResponse(entity.getId(), entity.getName(), entity.getContact());
    }
}
