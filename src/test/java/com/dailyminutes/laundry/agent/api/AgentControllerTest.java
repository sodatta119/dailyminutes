package com.dailyminutes.laundry.agent.api;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.dto.CreateAgentRequest;
import com.dailyminutes.laundry.agent.dto.AgentResponse;
import com.dailyminutes.laundry.agent.dto.UpdateAgentRequest;
import com.dailyminutes.laundry.agent.service.AgentQueryService;
import com.dailyminutes.laundry.agent.service.AgentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AgentController.class)
class AgentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AgentService agentService;
    @MockitoBean
    private AgentQueryService agentQueryService;

    private AgentResponse agentResponse;
    private CreateAgentRequest createAgentRequest;
    private UpdateAgentRequest updateAgentRequest;

    @BeforeEach
    void setUp() {
        agentResponse = new AgentResponse(1L, "Test Agent", AgentState.ACTIVE, 1L, "1234567890", "unique1", LocalDate.now(), null, AgentDesignation.FLEET_AGENT);
        createAgentRequest = new CreateAgentRequest("Test Agent", AgentState.ACTIVE, 1L, "1234567890", "unique1", LocalDate.now(), AgentDesignation.FLEET_AGENT);
        updateAgentRequest = new UpdateAgentRequest(1L, "Updated Agent", AgentState.INACTIVE, 2L, "0987654321", "unique2", LocalDate.now(), null, AgentDesignation.STORE_AGENT);
    }

    @Test
    void createAgent_shouldReturnCreated() throws Exception {
        when(agentService.createAgent(any())).thenReturn(agentResponse);
        mockMvc.perform(post("/agents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAgentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAgentById_shouldReturnAgent() throws Exception {
        when(agentQueryService.findAgentById(1L)).thenReturn(Optional.of(agentResponse));
        mockMvc.perform(get("/agents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Agent"));
    }

    @Test
    void updateAgent_shouldReturnOk() throws Exception {
        when(agentService.updateAgent(any())).thenReturn(agentResponse);
        mockMvc.perform(put("/agents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAgentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAgent_shouldReturnNoContent() throws Exception {
        doNothing().when(agentService).deleteAgent(1L);
        mockMvc.perform(delete("/agents/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void assignTeam_shouldReturnOk() throws Exception {
        when(agentService.assignTeam(1L, 2L)).thenReturn(agentResponse);
        mockMvc.perform(put("/agents/1/assign-team/2"))
                .andExpect(status().isOk());
    }

//    @Test
//    void unassignTeam_shouldReturnOk() throws Exception {
//        when(agentService.unassignTeam(1L)).thenReturn(agentResponse);
//        mockMvc.perform(put("/agents/1/unassign-team"))
//                .andExpect(status().isOk());
//    }
}