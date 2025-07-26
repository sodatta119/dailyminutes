package com.dailyminutes.laundry.store.api;


import com.dailyminutes.laundry.store.dto.CreateStoreRequest;
import com.dailyminutes.laundry.store.dto.StoreResponse;
import com.dailyminutes.laundry.store.dto.UpdateStoreRequest;
import com.dailyminutes.laundry.store.service.StoreQueryService;
import com.dailyminutes.laundry.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(StoreController.class)
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private StoreService storeService;
    @MockitoBean
    private StoreQueryService storeQueryService;

    private StoreResponse storeResponse;
    private CreateStoreRequest createStoreRequest;
    private UpdateStoreRequest updateStoreRequest;

    @BeforeEach
    void setUp() {
        storeResponse = new StoreResponse(1L, "Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        createStoreRequest = new CreateStoreRequest("Test Store", "123 Main St", "1234567890", "test@test.com", 1L);
        updateStoreRequest = new UpdateStoreRequest(1L, "Updated Store", "456 Main St", "0987654321", "updated@test.com", 2L);
    }

    @Test
    void createStore_shouldReturnCreated() throws Exception {
        when(storeService.createStore(any())).thenReturn(storeResponse);
        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createStoreRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getStoreById_shouldReturnStore() throws Exception {
        when(storeQueryService.findStoreById(1L)).thenReturn(Optional.of(storeResponse));
        mockMvc.perform(get("/stores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Store"));
    }

    @Test
    void updateStore_shouldReturnOk() throws Exception {
        when(storeService.updateStore(any())).thenReturn(storeResponse);
        mockMvc.perform(put("/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStoreRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStore_shouldReturnNoContent() throws Exception {
        doNothing().when(storeService).deleteStore(1L);
        mockMvc.perform(delete("/stores/1"))
                .andExpect(status().isNoContent());
    }
}
