/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 07/09/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.geofence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeofenceRegion(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("region_id") Long regionId,
        @JsonProperty("region_name") String regionName,
        @JsonProperty("region_description") String regionDescription,

        // nested array of coordinates
        @JsonProperty("region_data") List<List<RegionPoint>> regionData,

        @JsonProperty("is_active") Integer isActive,
        @JsonProperty("last_updated_at") String lastUpdatedAt,
        @JsonProperty("team_data") List<TeamData> teamData
) {}
