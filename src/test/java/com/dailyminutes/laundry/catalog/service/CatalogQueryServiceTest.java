package com.dailyminutes.laundry.catalog.service;


import com.dailyminutes.laundry.catalog.domain.model.CatalogEntity;
import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;
import com.dailyminutes.laundry.catalog.dto.CatalogResponse;
import com.dailyminutes.laundry.catalog.repository.CatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * The type Catalog query service test.
 */
@ExtendWith(MockitoExtension.class)
class CatalogQueryServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @InjectMocks
    private CatalogQueryService catalogQueryService;

    private CatalogEntity catalogEntity;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        catalogEntity = new CatalogEntity(1L, CatalogType.SERVICE, "Dry Cleaning", UnitType.KG, new BigDecimal(100));
    }

    /**
     * Find catalog by id should return catalog response when found.
     */
    @Test
    void findCatalogById_shouldReturnCatalogResponse_whenFound() {
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(catalogEntity));

        Optional<CatalogResponse> result = catalogQueryService.findCatalogById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(catalogEntity.getId());
    }

    /**
     * Find catalog by id should return empty when not found.
     */
    @Test
    void findCatalogById_shouldReturnEmpty_whenNotFound() {
        when(catalogRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CatalogResponse> result = catalogQueryService.findCatalogById(1L);

        assertThat(result).isNotPresent();
    }

    /**
     * Find all catalogs should return list of catalog responses.
     */
    @Test
    void findAllCatalogs_shouldReturnListOfCatalogResponses() {
        when(catalogRepository.findAll()).thenReturn(Collections.singletonList(catalogEntity));

        List<CatalogResponse> result = catalogQueryService.findAllCatalogs();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(catalogEntity.getId());
    }
}
