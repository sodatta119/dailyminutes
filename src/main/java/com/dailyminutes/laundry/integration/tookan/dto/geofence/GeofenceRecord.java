/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 27/08/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.geofence;

import com.dailyminutes.laundry.integration.tookan.dto.agent.FleetRecord;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * The type Geofence record.
 */
public record GeofenceRecord(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("region_id") Long regionId,
        @JsonProperty("region_name") String regionName,
        @JsonProperty("region_description") String regionDescription,
        @JsonProperty("region_data") List<RegionPoint> regionData,
        @JsonProperty("created_by") Long createdBy,
        @JsonProperty("is_active") Integer isActive,
        @JsonProperty("last_updated_at") OffsetDateTime lastUpdatedAt,
        List<FleetRecord> fleets,
        @JsonProperty("selected_team_id") List<Long> selectedTeamIds
) {}
