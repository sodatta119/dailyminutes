/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.agent.dto;

import com.dailyminutes.laundry.agent.domain.model.AgentDesignation;
import com.dailyminutes.laundry.agent.domain.model.AgentState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

/**
 * DTO for updating an existing Agent.
 */
public record UpdateAgentRequest(
        @NotNull(message = "Agent ID cannot be null for update")
        Long id,
        @NotBlank(message = "Agent name cannot be blank")
        String name,
        @NotNull(message = "Agent state cannot be null")
        AgentState state,
        Long teamId, // Optional: can be null for unassignment
        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
        String phoneNumber,
        @NotBlank(message = "Unique ID cannot be blank")
        String uniqueId,
        @NotNull(message = "Joining date cannot be null")
        LocalDate joiningDate,
        LocalDate terminationDate, // Optional: for setting termination date
        @NotNull(message = "Designation cannot be null")
        AgentDesignation designation
) {}
