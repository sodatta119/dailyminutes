package com.dailyminutes.laundry.geofence.api;


import com.dailyminutes.laundry.geofence.dto.CreateGeofenceRequest;
import com.dailyminutes.laundry.geofence.dto.GeofenceResponse;
import com.dailyminutes.laundry.geofence.dto.UpdateGeofenceRequest;
import com.dailyminutes.laundry.geofence.service.GeofenceQueryService;
import com.dailyminutes.laundry.geofence.service.GeofenceService;
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
@WebMvcTest(GeofenceController.class)
class GeofenceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private GeofenceService geofenceService;
    @MockitoBean
    private GeofenceQueryService geofenceQueryService;

    private GeofenceResponse geofenceResponse;
    private CreateGeofenceRequest createGeofenceRequest;
    private UpdateGeofenceRequest updateGeofenceRequest;

    @BeforeEach
    void setUp() {
        geofenceResponse = new GeofenceResponse(1L, "coords", "type", "name", true);
        createGeofenceRequest = new CreateGeofenceRequest("coords", "type", "name", true);
        updateGeofenceRequest = new UpdateGeofenceRequest(1L, "new-coords", "new-type", "new-name", false);
    }

    @Test
    void createGeofence_shouldReturnCreated() throws Exception {
        when(geofenceService.createGeofence(any())).thenReturn(geofenceResponse);
        mockMvc.perform(post("/geofences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createGeofenceRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getGeofenceById_shouldReturnGeofence() throws Exception {
        when(geofenceQueryService.findGeofenceById(1L)).thenReturn(Optional.of(geofenceResponse));
        mockMvc.perform(get("/geofences/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void updateGeofence_shouldReturnOk() throws Exception {
        when(geofenceService.updateGeofence(any())).thenReturn(geofenceResponse);
        mockMvc.perform(put("/geofences/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateGeofenceRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteGeofence_shouldReturnNoContent() throws Exception {
        doNothing().when(geofenceService).deleteGeofence(1L);
        mockMvc.perform(delete("/geofences/1"))
                .andExpect(status().isNoContent());
    }
}
