package com.dailyminutes.laundry.team.api;

import com.dailyminutes.laundry.team.domain.model.TeamRole;
import com.dailyminutes.laundry.team.dto.CreateTeamRequest;
import com.dailyminutes.laundry.team.dto.TeamResponse;
import com.dailyminutes.laundry.team.dto.UpdateTeamRequest;
import com.dailyminutes.laundry.team.service.TeamQueryService;
import com.dailyminutes.laundry.team.service.TeamService;
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
@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private TeamService teamService;
    @MockitoBean
    private TeamQueryService teamQueryService;

    private TeamResponse teamResponse;
    private CreateTeamRequest createTeamRequest;
    private UpdateTeamRequest updateTeamRequest;

    @BeforeEach
    void setUp() {
        teamResponse = new TeamResponse(1L, "Test Team", "desc", TeamRole.OPS);
        createTeamRequest = new CreateTeamRequest("Test Team", "desc", TeamRole.OPS);
        updateTeamRequest = new UpdateTeamRequest(1L, "Updated Team", "new desc", TeamRole.ADMIN);
    }

    @Test
    void createTeam_shouldReturnCreated() throws Exception {
        when(teamService.createTeam(any())).thenReturn(teamResponse);
        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTeamRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getTeamById_shouldReturnTeam() throws Exception {
        when(teamQueryService.findTeamById(1L)).thenReturn(Optional.of(teamResponse));
        mockMvc.perform(get("/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Team"));
    }

    @Test
    void updateTeam_shouldReturnOk() throws Exception {
        when(teamService.updateTeam(any())).thenReturn(teamResponse);
        mockMvc.perform(put("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTeamRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTeam_shouldReturnNoContent() throws Exception {
        doNothing().when(teamService).deleteTeam(1L);
        mockMvc.perform(delete("/teams/1"))
                .andExpect(status().isNoContent());
    }
}
