/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.service;


import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.dailyminutes.laundry.catalog.dto.CatalogOrderItemSummaryResponse;
import com.dailyminutes.laundry.catalog.dto.CatalogResponse;
import com.dailyminutes.laundry.catalog.repository.CatalogOrderItemSummaryRepository;
import com.dailyminutes.laundry.catalog.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogQueryService {

    private final CatalogRepository catalogRepository;
    private final CatalogOrderItemSummaryRepository catalogOrderItemSummaryRepository;

    public Optional<CatalogResponse> findCatalogById(Long id) {
        return catalogRepository.findById(id).map(this::toCatalogResponse);
    }

    public List<CatalogResponse> findAllCatalogs() {
        return StreamSupport.stream(catalogRepository.findAll().spliterator(), false)
                .map(this::toCatalogResponse)
                .collect(Collectors.toList());
    }


    public List<CatalogOrderItemSummaryResponse> findOrderItemSummariesByCatalogId(Long catalogId) {
        return catalogOrderItemSummaryRepository.findByCatalogId(catalogId).stream()
                .map(summary -> new CatalogOrderItemSummaryResponse(
                        summary.getId(),
                        summary.getCatalogId(),
                        summary.getCatalogName(),
                        summary.getCatalogType(),
                        summary.getUnitType(),
                        summary.getOrderId(),
                        summary.getOrderItemId(),
                        summary.getQuantity(),
                        summary.getItemPriceAtOrder(),
                        summary.getOrderDate()
                )).collect(Collectors.toList());
    }


    private CatalogResponse toCatalogResponse(CatalogEntity entity) {
        return new CatalogResponse(
                entity.getId(),
                entity.getType(),
                entity.getName()
        );
    }
}
