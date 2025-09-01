/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.team;

/**
 * The type Tookan team record.
 */
public record TookanTeamRecord(
        String tags,
        Integer battery_usage,
        Integer team_id,
        String team_name,
        String address,
        String latitude,
        String longitude
) {}