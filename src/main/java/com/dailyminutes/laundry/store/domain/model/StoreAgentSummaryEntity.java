/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.domain.model; // Updated package name

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Read model for Agent summary information relevant to the Store module.
 * This entity is populated via events from the Agent and Task modules.
 */
@Table("DL_STORE_AGENT_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreAgentSummaryEntity {

    @Id
    private Long id;

    private Long storeId;

    private Long agentId;

    private String agentName;

    private String agentPhoneNumber;

    private String agentDesignation;

    private String agentStatus;
}
