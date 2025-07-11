package com.chitchatfm.dailyminutes.laundry.agent.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

/**
 * The type Agent entity.
 */
@Table(name = "DL_AGENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentEntity {

    @Id
    private Long id;

    private String name;

    private AgentState state;

    private Long teamId; // Logical link to the Team module

    private String phoneNumber;

    private String uniqueId; // e.g., Employee ID, Agent Code

    private LocalDate joiningDate;

    private LocalDate terminationDate; // Better name for exit date

    private AgentDesignation designation;
}

