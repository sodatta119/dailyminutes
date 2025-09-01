/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.team;

import java.util.List;


/**
 * The type Tookan teams response.
 */
public record TookanTeamsResponse(
        String message,
        int status,
        List<TookanTeamRecord> data
) {}