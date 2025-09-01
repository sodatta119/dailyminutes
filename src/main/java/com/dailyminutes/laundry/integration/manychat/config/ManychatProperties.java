/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.manychat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Manychat properties.
 */
@ConfigurationProperties(prefix = "manychat")
public record ManychatProperties(
        String baseUrl,
        String token
) {}