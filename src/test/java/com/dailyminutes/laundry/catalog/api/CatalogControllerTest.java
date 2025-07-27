package com.dailyminutes.laundry.catalog.api;

import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;
import com.dailyminutes.laundry.catalog.dto.CatalogResponse;
import com.dailyminutes.laundry.catalog.dto.CreateCatalogRequest;
import com.dailyminutes.laundry.catalog.service.CatalogQueryService;
import com.dailyminutes.laundry.catalog.service.CatalogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CatalogController.class)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CatalogService catalogService;

    @MockitoBean
    private CatalogQueryService catalogQueryService;

    private CreateCatalogRequest createRequest;
    private CatalogResponse catalogResponse;

    @BeforeEach
    void setUp() {
        createRequest = new CreateCatalogRequest(CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("12.50"));
        catalogResponse = new CatalogResponse(1L, CatalogType.SERVICE, "Dry Cleaning", UnitType.PIECES, new BigDecimal("12.50"));
    }

    @Test
    void createCatalog_shouldReturnCreatedCatalog() throws Exception {
        when(catalogService.createCatalog(any(CreateCatalogRequest.class))).thenReturn(catalogResponse);

        mockMvc.perform(post("/api/catalogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(catalogResponse.id()))
                .andExpect(jsonPath("$.name").value(catalogResponse.name()));
    }
}
