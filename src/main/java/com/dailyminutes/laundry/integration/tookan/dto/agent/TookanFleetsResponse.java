/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 27/08/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.agent;


import java.util.List;

/**
 * The type Tookan fleets response.
 */
public record TookanFleetsResponse(
        String message,
        int status,
        List<FleetRecord> data
) {}
