/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.team.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


/**
 * The type Team entity.
 */
@Table(name = "DL_TEAM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamEntity {

    @Id
    private Long id;

    private Long externalId;

    private String name;

    private String description;

    private TeamRole role;
}

