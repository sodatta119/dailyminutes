/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.tookan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Tookan properties.
 */
@ConfigurationProperties(prefix = "tookan")
public record TookanProperties(
        String baseUrl,   // e.g. https://api.tookanapp.com
        String apiKey     // e.g. ${TOOKAN_API_KEY}
) {}