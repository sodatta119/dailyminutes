package com.dailyminutes.laundry.agent.api;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.dto.*;
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
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for AgentController.
 * Uses @WebMvcTest to test the controller layer in isolation, mocking service dependencies.
 */
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AgentController.class) // Focuses on testing AgentController
class AgentControllerTest {

    @Autowired
    private MockMvc mockMvc; // Used to simulate HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // Used to convert objects to JSON and vice-versa

    @MockitoBean // Creates a Mockito mock for AgentService and adds it to the Spring application context
    private AgentService agentService;

    @MockitoBean // Creates a Mockito mock for AgentQueryService and adds it to the Spring application context
    private AgentQueryService agentQueryService;

    // Common DTOs for testing
    private CreateAgentRequest createRequest;
    private UpdateAgentRequest updateRequest;
    private AgentResponse agentResponse;
    private AgentTeamSummaryResponse teamSummaryResponse;
    private AgentTaskSummaryResponse taskSummaryResponse;

    @BeforeEach
    void setUp() {
        // Initialize common DTOs for tests
        createRequest = new CreateAgentRequest(
                "Test Agent", AgentState.ACTIVE, 1L, "9876543210", "UID001", LocalDate.of(2023, 1, 1), AgentDesignation.FLEET_AGENT);

        updateRequest = new UpdateAgentRequest(
                1L, "Updated Agent", AgentState.INACTIVE, 2L, "9876543211", "UID001-Updated", LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), AgentDesignation.FLEET_AGENT);

        agentResponse = new AgentResponse(
                1L, "Test Agent", AgentState.ACTIVE, 1L, "9876543210", "UID001", LocalDate.of(2023, 1, 1), null, AgentDesignation.FLEET_AGENT);

        teamSummaryResponse = new AgentTeamSummaryResponse(
                100L, 10L, 1L, "Team Alpha", "Description Alpha");

        taskSummaryResponse = new AgentTaskSummaryResponse(
                200L, 1L, "Pickup Task", "PICKUP", "NEW", LocalDateTime.now(), "Source Addr", "Dest Addr", 500L);
    }

    @Test
    void createAgent_shouldReturnCreatedAgent_whenSuccessful() throws Exception {
        // Mock the service call to return a successful response
        when(agentService.createAgent(any(CreateAgentRequest.class))).thenReturn(agentResponse);

        // Perform POST request and assert the response
        mockMvc.perform(post("/agents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.id").value(agentResponse.id()))
                .andExpect(jsonPath("$.name").value(agentResponse.name()));
    }

//    @Test
//    void createAgent_shouldReturnBadRequest_whenValidationFails() throws Exception {
//        // Create an invalid request (e.g., blank name)
//        CreateAgentRequest invalidRequest = new CreateAgentRequest(
//                "", AgentState.ACTIVE, 1L, "9876543210", "UID001", LocalDate.of(2023, 1, 1), AgentDesignation.FLEET_AGENT);
//
//        // Perform POST request and assert the response
//        mockMvc.perform(post("/agents")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest()); // Expect HTTP 400 Bad Request
//    }
//
//    @Test
//    void createAgent_shouldReturnBadRequest_whenBusinessRuleFails() throws Exception {
//        // Mock the service call to throw an IllegalArgumentException (simulating business rule violation)
//        when(agentService.createAgent(any(CreateAgentRequest.class)))
//                .thenThrow(new IllegalArgumentException("Agent with unique ID UID001 already exists."));
//
//        // Perform POST request and assert the response
//        mockMvc.perform(post("/agents")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createRequest)))
//                .andExpect(status().isBadRequest()) // Expect HTTP 400 Bad Request
//                .andExpect(jsonPath("$.message").value("Agent with unique ID UID001 already exists."));
//    }
//
//    @Test
//    void updateAgent_shouldReturnUpdatedAgent_whenSuccessful() throws Exception {
//        // Mock the service call to return an updated response
//        when(agentService.updateAgent(any(UpdateAgentRequest.class))).thenReturn(agentResponse); // Reusing agentResponse for simplicity
//
//        // Perform PUT request and assert the response
//        mockMvc.perform(put("/agents/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk()) // Expect HTTP 200 OK
//                .andExpect(jsonPath("$.id").value(agentResponse.id()))
//                .andExpect(jsonPath("$.name").value(agentResponse.name()));
//    }
//
//    @Test
//    void updateAgent_shouldReturnBadRequest_whenIdMismatch() throws Exception {
//        // Perform PUT request with mismatching IDs
//        mockMvc.perform(put("/agents/{id}", 99L) // Path ID 99L
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest))) // Request body ID 1L
//                .andExpect(status().isBadRequest()) // Expect HTTP 400 Bad Request
//                .andExpect(jsonPath("$.message").value("ID in path must match ID in request body."));
//    }
//
//    @Test
//    void updateAgent_shouldReturnNotFound_whenAgentDoesNotExist() throws Exception {
//        // Mock the service call to throw an IllegalArgumentException (simulating agent not found)
//        when(agentService.updateAgent(any(UpdateAgentRequest.class)))
//                .thenThrow(new IllegalArgumentException("Agent with ID 1 not found."));
//
//        // Perform PUT request and assert the response
//        mockMvc.perform(put("/agents/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isNotFound()) // Expect HTTP 404 Not Found
//                .andExpect(jsonPath("$.message").value("Agent with ID 1 not found."));
//    }
//
//    @Test
//    void deleteAgent_shouldReturnNoContent_whenSuccessful() throws Exception {
//        // Mock the service call to do nothing (successful deletion)
//        doNothing().when(agentService).deleteAgent(anyLong());
//
//        // Perform DELETE request and assert the response
//        mockMvc.perform(delete("/agents/{id}", 1L))
//                .andExpect(status().isNoContent()); // Expect HTTP 204 No Content
//    }
//
//    @Test
//    void deleteAgent_shouldReturnNotFound_whenAgentDoesNotExist() throws Exception {
//        // Mock the service call to throw an IllegalArgumentException (simulating agent not found)
//        doThrow(new IllegalArgumentException("Agent with ID 99 not found."))
//                .when(agentService).deleteAgent(anyLong());
//
//        // Perform DELETE request and assert the response
//        mockMvc.perform(delete("/agents/{id}", 99L))
//                .andExpect(status().isNotFound()) // Expect HTTP 404 Not Found
//                .andExpect(jsonPath("$.message").value("Agent with ID 99 not found."));
//    }
//
//    @Test
//    void getAgentById_shouldReturnAgent_whenFound() throws Exception {
//        // Mock the query service call
//        when(agentQueryService.findAgentById(1L)).thenReturn(Optional.of(agentResponse));
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/{id}", 1L))
//                .andExpect(status().isOk()) // Expect HTTP 200 OK
//                .andExpect(jsonPath("$.id").value(agentResponse.id()))
//                .andExpect(jsonPath("$.name").value(agentResponse.name()));
//    }
//
//    @Test
//    void getAgentById_shouldReturnNotFound_whenNotFound() throws Exception {
//        // Mock the query service call to return empty
//        when(agentQueryService.findAgentById(99L)).thenReturn(Optional.empty());
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/{id}", 99L))
//                .andExpect(status().isNotFound()) // Expect HTTP 404 Not Found
//                .andExpect(jsonPath("$.message").value("Agent with ID 99 not found."));
//    }
//
//    @Test
//    void getAllAgents_shouldReturnListOfAgents() throws Exception {
//        // Mock the query service call
//        when(agentQueryService.findAllAgents()).thenReturn(Arrays.asList(agentResponse,
//                new AgentResponse(2L, "Another Agent", AgentState.ACTIVE, null, "1234567890", "UID002", LocalDate.now(), null, AgentDesignation.CUSTOMER_SUPPORT)));
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents"))
//                .andExpect(status().isOk()) // Expect HTTP 200 OK
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].id").value(agentResponse.id()));
//    }
//
//    @Test
//    void getAgentsByTeamId_shouldReturnListOfAgents_whenFound() throws Exception {
//        // Mock the query service call
//        when(agentQueryService.findAgentsByTeamId(1L)).thenReturn(Collections.singletonList(agentResponse));
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/by-team/{teamId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].teamId").value(1L));
//    }
//
//    @Test
//    void getAgentsByTeamId_shouldReturnNotFound_whenNoAgentsFound() throws Exception {
//        // Mock the query service call to return an empty list
//        when(agentQueryService.findAgentsByTeamId(99L)).thenReturn(Collections.emptyList());
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/by-team/{teamId}", 99L))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("No agents found for team ID 99"));
//    }
//
//    @Test
//    void getAgentsByState_shouldReturnListOfAgents_whenFound() throws Exception {
//        // Mock the query service call
//        when(agentQueryService.findAgentsByState(AgentState.ACTIVE)).thenReturn(Collections.singletonList(agentResponse));
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/by-state/{state}", AgentState.ACTIVE))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].state").value(AgentState.ACTIVE.name()));
//    }
//
//    @Test
//    void getAgentsByState_shouldReturnNotFound_whenNoAgentsFound() throws Exception {
//        // Mock the query service call to return an empty list
//        when(agentQueryService.findAgentsByState(AgentState.INACTIVE)).thenReturn(Collections.emptyList());
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/by-state/{state}", AgentState.INACTIVE))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("No agents found with state INACTIVE"));
//    }
//
//    @Test
//    void getAgentsByDesignation_shouldReturnListOfAgents_whenFound() throws Exception {
//        // Mock the query service call
//        when(agentQueryService.findAgentsByDesignation(AgentDesignation.FLEET_AGENT)).thenReturn(Collections.singletonList(agentResponse));
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/by-designation/{designation}", AgentDesignation.FLEET_AGENT))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].designation").value(AgentDesignation.FLEET_AGENT.name()));
//    }
//
//    @Test
//    void getAgentsByDesignation_shouldReturnNotFound_whenNoAgentsFound() throws Exception {
//        // Mock the query service call to return an empty list
//        when(agentQueryService.findAgentsByDesignation(AgentDesignation.PROCESS_MANAGER)).thenReturn(Collections.emptyList());
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/by-designation/{designation}", AgentDesignation.PROCESS_MANAGER))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("No agents found with designation PROCESS_MANAGER"));
//    }
//
//    @Test
//    void getAgentTeamSummaries_shouldReturnListOfSummaries_whenFound() throws Exception {
//        // Mock the query service call
//        when(agentQueryService.findAgentTeamSummariesByAgentId(1L)).thenReturn(Collections.singletonList(teamSummaryResponse));
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/{agentId}/team-summaries", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].agentId").value(teamSummaryResponse.agentId()))
//                .andExpect(jsonPath("$[0].teamName").value(teamSummaryResponse.teamName()));
//    }
//
//    @Test
//    void getAgentTeamSummaries_shouldReturnNotFound_whenNoSummariesFound() throws Exception {
//        // Mock the query service call to return an empty list
//        when(agentQueryService.findAgentTeamSummariesByAgentId(99L)).thenReturn(Collections.emptyList());
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/{agentId}/team-summaries", 99L))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("No team summaries found for agent ID 99"));
//    }
//
//    @Test
//    void getAgentTaskSummaries_shouldReturnListOfSummaries_whenFound() throws Exception {
//        // Mock the query service call
//        when(agentQueryService.findAgentTaskSummariesByAgentId(1L)).thenReturn(Collections.singletonList(taskSummaryResponse));
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/{agentId}/task-summaries", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].agentId").value(taskSummaryResponse.agentId()))
//                .andExpect(jsonPath("$[0].taskName").value(taskSummaryResponse.taskName()));
//    }
//
//    @Test
//    void getAgentTaskSummaries_shouldReturnNotFound_whenNoSummariesFound() throws Exception {
//        // Mock the query service call to return an empty list
//        when(agentQueryService.findAgentTaskSummariesByAgentId(99L)).thenReturn(Collections.emptyList());
//
//        // Perform GET request and assert the response
//        mockMvc.perform(get("/agents/{agentId}/task-summaries", 99L))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("No task summaries found for agent ID 99"));
//    }
}
