/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.tookan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

// com.dailyminutes.laundry.integration.tookan.SimpleTookanConfig.java
@Configuration
@ConfigurationProperties(prefix = "tookan")
public class SimpleTookanConfig {
    /** e.g. https://api.tookanapp.com */
    private URI baseUrl=URI.create("");
    /** supply via env: TOOKAN_API_KEY */
    private String apiKey;

    @Bean
    WebClient tookanClient() {
        return WebClient.builder()
                .baseUrl(baseUrl.toString())
                .defaultHeader("Authorization", apiKey) // or whatever Tookan expects
                .build();
    }

    // getters/setters
}
