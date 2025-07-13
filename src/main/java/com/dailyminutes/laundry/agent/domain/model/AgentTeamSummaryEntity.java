/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.agent.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Read model for Team summary information relevant to the Agent module.
 * This entity is populated via events from the Team module.
 */
@Table("DL_AGENT_TEAM_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AgentTeamSummaryEntity {

    @Id
    private Long id;

    private Long agentId;

    private Long teamId;

    private String teamName;

    private String teamDescription;
}

