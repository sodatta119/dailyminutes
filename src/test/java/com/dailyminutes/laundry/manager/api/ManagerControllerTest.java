package com.dailyminutes.laundry.manager.api;


import com.dailyminutes.laundry.manager.dto.CreateManagerRequest;
import com.dailyminutes.laundry.manager.dto.ManagerResponse;
import com.dailyminutes.laundry.manager.dto.UpdateManagerRequest;
import com.dailyminutes.laundry.manager.service.ManagerQueryService;
import com.dailyminutes.laundry.manager.service.ManagerService;
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
@WebMvcTest(ManagerController.class)
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ManagerService managerService;
    @MockitoBean
    private ManagerQueryService managerQueryService;

    private ManagerResponse managerResponse;
    private CreateManagerRequest createManagerRequest;
    private UpdateManagerRequest updateManagerRequest;

    @BeforeEach
    void setUp() {
        managerResponse = new ManagerResponse(1L, "Test Manager", "test@contact.com");
        createManagerRequest = new CreateManagerRequest("Test Manager", "test@contact.com");
        updateManagerRequest = new UpdateManagerRequest(1L, "Updated Manager", "updated@contact.com");
    }

    @Test
    void createManager_shouldReturnCreated() throws Exception {
        when(managerService.createManager(any())).thenReturn(managerResponse);
        mockMvc.perform(post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createManagerRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getManagerById_shouldReturnManager() throws Exception {
        when(managerQueryService.findManagerById(1L)).thenReturn(Optional.of(managerResponse));
        mockMvc.perform(get("/managers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Manager"));
    }

    @Test
    void updateManager_shouldReturnOk() throws Exception {
        when(managerService.updateManager(any())).thenReturn(managerResponse);
        mockMvc.perform(put("/managers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateManagerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteManager_shouldReturnNoContent() throws Exception {
        doNothing().when(managerService).deleteManager(1L);
        mockMvc.perform(delete("/managers/1"))
                .andExpect(status().isNoContent());
    }
}
