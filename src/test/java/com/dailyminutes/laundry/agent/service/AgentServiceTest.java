package com.dailyminutes.laundry.agent.service;


import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentEntity;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import com.dailyminutes.laundry.agent.dto.AgentResponse;
import com.dailyminutes.laundry.agent.dto.CreateAgentRequest;
import com.dailyminutes.laundry.agent.dto.UpdateAgentRequest;
import com.dailyminutes.laundry.agent.domain.event.AgentAssignedToTeamEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentCreatedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentDeletedEvent;
import com.dailyminutes.laundry.agent.domain.event.AgentUpdatedEvent;
import com.dailyminutes.laundry.agent.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito annotations
class AgentServiceTest {

    @Mock // Mocks the AgentRepository dependency
    private AgentRepository agentRepository;

    @Mock // Mocks the ApplicationEventPublisher dependency
    private ApplicationEventPublisher events;

    @InjectMocks // Injects the mocks into AgentService
    private AgentService agentService;

    // Common data for tests
    private CreateAgentRequest createRequest;
    private UpdateAgentRequest updateRequest;
    private AgentEntity agentEntity;

    @BeforeEach
    void setUp() {
        // Initialize common request DTOs and entity for tests
        createRequest = new CreateAgentRequest(
                "New Agent", AgentState.ACTIVE, 1L, "9876543210", "UID001", LocalDate.now(), AgentDesignation.FLEET_AGENT);

        updateRequest = new UpdateAgentRequest(
                1L, "Updated Agent", AgentState.INACTIVE, 2L, "9876543211", "UID001-Updated", LocalDate.now(), LocalDate.now().plusDays(10), AgentDesignation.FLEET_AGENT);

        agentEntity = new AgentEntity(
                1L, "New Agent", AgentState.ACTIVE, 1L, "9876543210", "UID001", LocalDate.now(), null, AgentDesignation.FLEET_AGENT);
    }

    @Test
    void createAgent_shouldCreateAndPublishEvent() {
        // Mock repository behavior
        when(agentRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(agentRepository.findByUniqueId(anyString())).thenReturn(Optional.empty());
        when(agentRepository.save(any(AgentEntity.class))).thenReturn(agentEntity); // Return a saved entity with ID

        // Call the service method
        AgentResponse response = agentService.createAgent(createRequest);

        // Verify repository interaction
        verify(agentRepository, times(1)).findByPhoneNumber(createRequest.phoneNumber());
        verify(agentRepository, times(1)).findByUniqueId(createRequest.uniqueId());
        verify(agentRepository, times(1)).save(any(AgentEntity.class));

        // Verify event publishing
        ArgumentCaptor<AgentCreatedEvent> eventCaptor = ArgumentCaptor.forClass(AgentCreatedEvent.class);
        verify(events, times(1)).publishEvent(eventCaptor.capture());
        AgentCreatedEvent capturedEvent = eventCaptor.getValue();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(agentEntity.getId());
        assertThat(response.name()).isEqualTo(createRequest.name());
        assertThat(capturedEvent.agentId()).isEqualTo(agentEntity.getId());
        assertThat(capturedEvent.name()).isEqualTo(createRequest.name());
    }

    @Test
    void createAgent_shouldThrowException_whenPhoneNumberExists() {
        when(agentRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(new AgentEntity())); // Simulate existing phone number

        // Assert that an IllegalArgumentException is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                agentService.createAgent(createRequest)
        );

        assertThat(exception.getMessage()).contains("phone number " + createRequest.phoneNumber() + " already exists");
        verify(agentRepository, never()).save(any(AgentEntity.class)); // Ensure save is not called
        verify(events, never()).publishEvent(any()); // Ensure no event is published
    }

    @Test
    void createAgent_shouldThrowException_whenUniqueIdExists() {
        when(agentRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(agentRepository.findByUniqueId(anyString())).thenReturn(Optional.of(new AgentEntity())); // Simulate existing unique ID

        // Assert that an IllegalArgumentException is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                agentService.createAgent(createRequest)
        );

        assertThat(exception.getMessage()).contains("unique ID " + createRequest.uniqueId() + " already exists");
        verify(agentRepository, never()).save(any(AgentEntity.class)); // Ensure save is not called
        verify(events, never()).publishEvent(any()); // Ensure no event is published
    }

    @Test
    void updateAgent_shouldUpdateAndPublishEvent() {
        // Mock repository behavior
        when(agentRepository.findById(agentEntity.getId())).thenReturn(Optional.of(agentEntity));
        when(agentRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty()); // No conflict
        when(agentRepository.findByUniqueId(anyString())).thenReturn(Optional.empty()); // No conflict
        when(agentRepository.save(any(AgentEntity.class))).thenReturn(new AgentEntity( // Return updated entity
                agentEntity.getId(), updateRequest.name(), updateRequest.state(), updateRequest.teamId(),
                updateRequest.phoneNumber(), updateRequest.uniqueId(), updateRequest.joiningDate(),
                updateRequest.terminationDate(), updateRequest.designation()
        ));

        // Call the service method
        AgentResponse response = agentService.updateAgent(updateRequest);

        // Verify repository interaction
        verify(agentRepository, times(1)).findById(updateRequest.id());
        verify(agentRepository, times(1)).save(any(AgentEntity.class));

        // Verify AgentUpdatedEvent is published
        ArgumentCaptor<AgentUpdatedEvent> updatedEventCaptor = ArgumentCaptor.forClass(AgentUpdatedEvent.class);
        verify(events, times(1)).publishEvent(updatedEventCaptor.capture());
        AgentUpdatedEvent capturedUpdatedEvent = updatedEventCaptor.getValue();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(updateRequest.id());
        assertThat(response.name()).isEqualTo(updateRequest.name());
        assertThat(response.state()).isEqualTo(updateRequest.state());
        assertThat(capturedUpdatedEvent.agentId()).isEqualTo(updateRequest.id());
        assertThat(capturedUpdatedEvent.name()).isEqualTo(updateRequest.name());

        // Verify AgentAssignedToTeamEvent is published because teamId changed (1L to 2L)
        ArgumentCaptor<AgentAssignedToTeamEvent> teamEventCaptor = ArgumentCaptor.forClass(AgentAssignedToTeamEvent.class);
        verify(events, times(1)).publishEvent(teamEventCaptor.capture());
        AgentAssignedToTeamEvent capturedTeamEvent = teamEventCaptor.getValue();
        assertThat(capturedTeamEvent.agentId()).isEqualTo(updateRequest.id());
        assertThat(capturedTeamEvent.teamId()).isEqualTo(2L);
    }

    @Test
    void updateAgent_shouldNotPublishTeamEvent_whenTeamIdUnchanged() {
        UpdateAgentRequest sameTeamUpdateRequest = new UpdateAgentRequest(
                1L, "Updated Agent", AgentState.INACTIVE, 1L, "9876543211", "UID001-Updated", LocalDate.now(), LocalDate.now().plusDays(10), AgentDesignation.FLEET_AGENT);

        // Mock repository behavior
        when(agentRepository.findById(agentEntity.getId())).thenReturn(Optional.of(agentEntity));
        when(agentRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(agentRepository.findByUniqueId(anyString())).thenReturn(Optional.empty());
        when(agentRepository.save(any(AgentEntity.class))).thenReturn(new AgentEntity(
                agentEntity.getId(), sameTeamUpdateRequest.name(), sameTeamUpdateRequest.state(), sameTeamUpdateRequest.teamId(),
                sameTeamUpdateRequest.phoneNumber(), sameTeamUpdateRequest.uniqueId(), sameTeamUpdateRequest.joiningDate(),
                sameTeamUpdateRequest.terminationDate(), sameTeamUpdateRequest.designation()
        ));

        // Call the service method
        agentService.updateAgent(sameTeamUpdateRequest);

        // Verify AgentUpdatedEvent is published
        verify(events, times(1)).publishEvent(any(AgentUpdatedEvent.class));
        // Verify AgentAssignedToTeamEvent is NOT published
        verify(events, never()).publishEvent(any(AgentAssignedToTeamEvent.class));
    }

    @Test
    void updateAgent_shouldThrowException_whenAgentNotFound() {
        when(agentRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                agentService.updateAgent(updateRequest)
        );

        assertThat(exception.getMessage()).contains("Agent with ID " + updateRequest.id() + " not found.");
        verify(agentRepository, never()).save(any(AgentEntity.class));
        verify(events, never()).publishEvent(any());
    }

    @Test
    void updateAgent_shouldThrowException_whenPhoneNumberConflict() {
        AgentEntity conflictingAgent = new AgentEntity(2L, "Conflicting Agent", AgentState.ACTIVE, null, updateRequest.phoneNumber(), "ANOTHER_UID", LocalDate.now(), null, AgentDesignation.FLEET_AGENT);
        when(agentRepository.findById(agentEntity.getId())).thenReturn(Optional.of(agentEntity));
        when(agentRepository.findByPhoneNumber(updateRequest.phoneNumber())).thenReturn(Optional.of(conflictingAgent)); // Simulate conflict

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                agentService.updateAgent(updateRequest)
        );

        assertThat(exception.getMessage()).contains("phone number " + updateRequest.phoneNumber() + " already exists for another agent.");
        verify(agentRepository, never()).save(any(AgentEntity.class));
        verify(events, never()).publishEvent(any());
    }

    @Test
    void deleteAgent_shouldDeleteAndPublishEvent() {
        Long agentIdToDelete = 1L;
        when(agentRepository.existsById(agentIdToDelete)).thenReturn(true);
        doNothing().when(agentRepository).deleteById(agentIdToDelete);

        agentService.deleteAgent(agentIdToDelete);

        verify(agentRepository, times(1)).existsById(agentIdToDelete);
        verify(agentRepository, times(1)).deleteById(agentIdToDelete);

        ArgumentCaptor<AgentDeletedEvent> eventCaptor = ArgumentCaptor.forClass(AgentDeletedEvent.class);
        verify(events, times(1)).publishEvent(eventCaptor.capture());
        AgentDeletedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.agentId()).isEqualTo(agentIdToDelete);
    }

    @Test
    void deleteAgent_shouldThrowException_whenAgentNotFound() {
        Long agentIdToDelete = 99L;
        when(agentRepository.existsById(agentIdToDelete)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                agentService.deleteAgent(agentIdToDelete)
        );

        assertThat(exception.getMessage()).contains("Agent with ID " + agentIdToDelete + " not found.");
        verify(agentRepository, never()).deleteById(anyLong());
        verify(events, never()).publishEvent(any());
    }
}
