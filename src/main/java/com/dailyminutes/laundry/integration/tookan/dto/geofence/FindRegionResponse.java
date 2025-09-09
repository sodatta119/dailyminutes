/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 07/09/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.geofence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FindRegionResponse(
        String message,
        int status,
        List<GeofenceRegion> data
) {}