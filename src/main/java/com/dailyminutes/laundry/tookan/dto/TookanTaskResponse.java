/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 24/08/25
 */
package com.dailyminutes.laundry.tookan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TookanTaskResponse(
        String message,
        int status,
        @JsonProperty("job_id") Long jobId,
        @JsonProperty("pickup_job_id") Long pickupJobId,
        @JsonProperty("delivery_job_id") Long deliveryJobId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("order_id") String orderId
) {}
