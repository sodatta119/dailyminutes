/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 27/08/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * The type Fleet record.
 */
public record FleetRecord(
        @JsonProperty("fleet_id") Long fleetId,
        @JsonProperty("device_type") Integer deviceType,
        @JsonProperty("total_rating") Integer totalRating,
        @JsonProperty("total_rated_tasks") Integer totalRatedTasks,
        @JsonProperty("is_active") Integer isActive,
        @JsonProperty("has_gps_accuracy") Integer hasGpsAccuracy,
        String username,
        String name,
        @JsonProperty("team_id") Long teamId,
        @JsonProperty("login_id") String loginId,
        @JsonProperty("transport_type") Integer transportType,
        @JsonProperty("transport_desc") String transportDesc,
        String license,
        String email,
        String phone,
        @JsonProperty("battery_level") Integer batteryLevel,
        @JsonProperty("registration_status") Integer registrationStatus,
        Double latitude,
        Double longitude,
        String tags,
        @JsonProperty("fleet_thumb_image") String fleetThumbImage,
        @JsonProperty("fleet_image") String fleetImage,
        Integer status,
        String timezone,
        @JsonProperty("fleet_type") Integer fleetType,
        @JsonProperty("block_reason") String blockReason,
        @JsonProperty("location_update_datetime") OffsetDateTime locationUpdateDatetime,
        @JsonProperty("is_available") Integer isAvailable
) {}
