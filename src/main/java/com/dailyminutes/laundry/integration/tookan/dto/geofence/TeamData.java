/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 07/09/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.geofence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamData(
        @JsonProperty("team_id") Long teamId,
        @JsonProperty("team_name") String teamName,
        @JsonProperty("tags") String tags
) {}