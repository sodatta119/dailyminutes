/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */

package com.dailyminutes.laundry.agent.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String externalId;

    private LocalDateTime externalSyncAt;

    private Boolean isDeleted;

    private Double latitude;

    private Double longitude;

    private int active;

    private int available;

    private int batteryLevel;
}

