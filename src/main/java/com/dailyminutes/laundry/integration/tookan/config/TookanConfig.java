/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.tookan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The type Tookan config.
 */
// com.dailyminutes.laundry.integration.tookan.SimpleTookanConfig.java
@Configuration
@ConfigurationProperties(prefix = "tookan")
@EnableConfigurationProperties(TookanProperties.class)
public class TookanConfig {

    /**
     * Tookan client web client.
     *
     * @param props the props
     * @return the web client
     */
    @Bean
    WebClient tookanClient(TookanProperties props) {
        return WebClient.builder()
                .baseUrl(props.baseUrl())
                // Tookan’s team API expects api_key in the POST body, not in Authorization,
                // so you can drop the header if you like. Keeping it won’t hurt.
                .defaultHeader("Authorization", props.apiKey() == null ? "" : props.apiKey())
                .build();
    }

    // getters/setters
}
