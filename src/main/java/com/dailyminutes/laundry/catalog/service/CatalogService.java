/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.service;

import com.dailyminutes.laundry.catalog.domain.event.CatalogCreatedEvent;
import com.dailyminutes.laundry.catalog.domain.event.CatalogDeletedEvent;
import com.dailyminutes.laundry.catalog.domain.event.CatalogUpdatedEvent;
import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.dailyminutes.laundry.catalog.dto.CatalogResponse;
import com.dailyminutes.laundry.catalog.dto.CreateCatalogRequest;
import com.dailyminutes.laundry.catalog.dto.UpdateCatalogRequest;
import com.dailyminutes.laundry.catalog.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final ApplicationEventPublisher events;

    public CatalogResponse createCatalog(CreateCatalogRequest request) {
        CatalogEntity catalog = new CatalogEntity(
                null,
                request.type(),
                request.name(),
                request.unitType(),
                request.unitPrice()
        );
        CatalogEntity savedCatalog = catalogRepository.save(catalog);

        events.publishEvent(new CatalogCreatedEvent(
                savedCatalog.getId(),
                savedCatalog.getType(),
                savedCatalog.getName()
        ));
        return toCatalogResponse(savedCatalog);
    }

    public CatalogResponse updateCatalog(UpdateCatalogRequest request) {
        CatalogEntity existingCatalog = catalogRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Catalog with ID " + request.id() + " not found."));

        existingCatalog.setType(request.type());
        existingCatalog.setName(request.name());

        CatalogEntity updatedCatalog = catalogRepository.save(existingCatalog);

        events.publishEvent(new CatalogUpdatedEvent(
                updatedCatalog.getId(),
                updatedCatalog.getType(),
                updatedCatalog.getName()
        ));

        return toCatalogResponse(updatedCatalog);
    }

    public void deleteCatalog(Long id) {
        if (!catalogRepository.existsById(id)) {
            throw new IllegalArgumentException("Catalog with ID " + id + " not found.");
        }
        catalogRepository.deleteById(id);
        events.publishEvent(new CatalogDeletedEvent(id));
    }

    private CatalogResponse toCatalogResponse(CatalogEntity entity) {
        return new CatalogResponse(
                entity.getId(),
                entity.getType(),
                entity.getName()
        );
    }
}
