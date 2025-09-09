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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Store controller test.
 */
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

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        storeResponse = new StoreResponse(1L, "Test Store", "123 Main St", "1234567890", "test@test.com", 1L,"000","000");
        createStoreRequest = new CreateStoreRequest("Test Store", "123 Main St", "1234567890", "test@test.com", 1L,"00","00");
        updateStoreRequest = new UpdateStoreRequest(1L, "Updated Store", "456 Main St", "0987654321", "updated@test.com", 2L);
    }

    /**
     * Create store should return created.
     *
     * @throws Exception the exception
     */
    @Test
    void createStore_shouldReturnCreated() throws Exception {
        when(storeService.createStore(any())).thenReturn(storeResponse);
        mockMvc.perform(post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createStoreRequest)))
                .andExpect(status().isCreated());
    }

    /**
     * Gets store by id should return store.
     *
     * @throws Exception the exception
     */
    @Test
    void getStoreById_shouldReturnStore() throws Exception {
        when(storeQueryService.findStoreById(1L)).thenReturn(Optional.of(storeResponse));
        mockMvc.perform(get("/api/stores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Store"));
    }

    /**
     * Update store should return ok.
     *
     * @throws Exception the exception
     */
    @Test
    void updateStore_shouldReturnOk() throws Exception {
        when(storeService.updateStore(any())).thenReturn(storeResponse);
        mockMvc.perform(put("/api/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStoreRequest)))
                .andExpect(status().isOk());
    }

    /**
     * Delete store should return no content.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteStore_shouldReturnNoContent() throws Exception {
        doNothing().when(storeService).deleteStore(1L);
        mockMvc.perform(delete("/api/stores/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Add catalog item to store should return ok.
     *
     * @throws Exception the exception
     */
    @Test
    void addCatalogItemToStore_shouldReturnOk() throws Exception {
        // Given
        Long storeId = 1L;
        Long catalogId = 101L;
        doNothing().when(storeService).addCatalogItemToStore(storeId, catalogId);

        // When & Then
        mockMvc.perform(put("/api/stores/{storeId}/catalog/{catalogId}", storeId, catalogId))
                .andExpect(status().isOk());

        // Also verify that the service method was called with the correct parameters
        verify(storeService).addCatalogItemToStore(storeId, catalogId);
    }

    /**
     * Remove catalog item from store should return no content.
     *
     * @throws Exception the exception
     */
    @Test
    void removeCatalogItemFromStore_shouldReturnNoContent() throws Exception {
        // Given
        Long storeId = 1L;
        Long catalogId = 101L;
        doNothing().when(storeService).removeCatalogItemFromStore(storeId, catalogId);

        // When & Then
        mockMvc.perform(delete("/api/stores/{storeId}/catalog/{catalogId}", storeId, catalogId))
                .andExpect(status().isNoContent());

        verify(storeService).removeCatalogItemFromStore(storeId, catalogId);
    }

    /**
     * Remove geofence from store should return no content.
     *
     * @throws Exception the exception
     */
    @Test
    void removeGeofenceFromStore_shouldReturnNoContent() throws Exception {
        // Given
        Long storeId = 1L;
        Long geofenceId = 100L;
        doNothing().when(storeService).removeGeofenceFromStore(storeId, geofenceId);

        // When & Then
        mockMvc.perform(delete("/api/stores/{storeId}/geofences/{geofenceId}", storeId, geofenceId))
                .andExpect(status().isNoContent());

        verify(storeService).removeGeofenceFromStore(storeId, geofenceId);
    }

    /**
     * Assign geofence to store should return ok.
     *
     * @throws Exception the exception
     */
    @Test
    void assignGeofenceToStore_shouldReturnOk() throws Exception {
        // Given
        Long storeId = 1L;
        Long geofenceId = 100L;
        doNothing().when(storeService).assignGeofenceToStore(storeId, geofenceId);

        // When & Then
        mockMvc.perform(put("/api/stores/{storeId}/geofences/{geofenceId}", storeId, geofenceId))
                .andExpect(status().isOk());

        // Verify that the service method was called with the correct parameters
        verify(storeService).assignGeofenceToStore(storeId, geofenceId);
    }
}
