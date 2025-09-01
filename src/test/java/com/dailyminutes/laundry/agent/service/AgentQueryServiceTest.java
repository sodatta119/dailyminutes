package com.dailyminutes.laundry.agent.service;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * The type Agent query service test.
 */
@ExtendWith(MockitoExtension.class)
class AgentQueryServiceTest {

    @Mock
    private AgentRepository agentRepository;
    @InjectMocks
    private AgentQueryService agentQueryService;

    /**
     * Find agent by id should return agent.
     */
    @Test
    void findAgentById_shouldReturnAgent() {
        AgentEntity agent = new AgentEntity(1L, "Test Agent", AgentState.ACTIVE, 1L, "1234567890", "unique1", LocalDate.now(), null, AgentDesignation.FLEET_AGENT, "99L", LocalDateTime.now(), false, Double.valueOf(1d), Double.valueOf(1d), 1, 1,100);
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));

        assertThat(agentQueryService.findAgentById(1L)).isPresent();
    }
}