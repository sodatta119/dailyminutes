/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 24/08/25
 */
package com.dailyminutes.laundry.tookan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// Main request body
public record CreateTookanTaskRequest(
        @JsonProperty("api_key") String apiKey,
        @JsonProperty("order_id") String orderId,
        @JsonProperty("team_id") String teamId,
        @JsonProperty("auto_assignment") int autoAssignment,
        @JsonProperty("job_description") String jobDescription,
        @JsonProperty("job_pickup_phone") String jobPickupPhone,
        @JsonProperty("job_pickup_name") String jobPickupName,
        @JsonProperty("job_pickup_address") String jobPickupAddress,
        @JsonProperty("job_pickup_datetime") String jobPickupDatetime,
        @JsonProperty("job_delivery_datetime") String jobDeliveryDatetime,
        @JsonProperty("has_pickup") int hasPickup,
        @JsonProperty("has_delivery") int hasDelivery,
        @JsonProperty("layout_type") int layoutType,
        @JsonProperty("tracking_link") int trackingLink,
        @JsonProperty("timezone") String timezone,
        @JsonProperty("fleet_id") String fleetId,
        @JsonProperty("meta_data") List<MetaData> metaData
) {}

// Nested record for custom metadata
record MetaData(
        String label,
        String value
) {}