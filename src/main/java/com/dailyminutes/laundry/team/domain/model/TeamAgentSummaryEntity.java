/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.team.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Read model for Agent summary information relevant to the Team module.
 * This entity is populated via events from the Agent module.
 */
@Table("DL_TEAM_AGENT_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamAgentSummaryEntity {

    @Id
    private Long id;

    private Long teamId;

    private Long agentId;

    private String agentName;

    private String agentPhoneNumber;

    private String agentDesignation;

    private String agentState;
}