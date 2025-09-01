/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.manychat.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The type Manychat config.
 */
// com.dailyminutes.laundry.integration.tookan.SimpleTookanConfig.java
@Configuration
@ConfigurationProperties(prefix = "manychat")
@EnableConfigurationProperties(ManychatProperties.class)
public class ManychatConfig {

    /**
     * Many chat client web client.
     *
     * @param props the props
     * @return the web client
     */
    @Bean
    @ConditionalOnProperty(prefix = "manychat", name = "base-url")
    WebClient manyChatClient(ManychatProperties props) {
        return WebClient.builder()
                .baseUrl(props.baseUrl())
                .defaultHeaders(h -> {
                    if (props.token() != null && !props.token().isBlank()) {
                        h.setBearerAuth(props.token());
                    }
                    h.set("accept", "application/json");
                })
                .build();
    }

    // getters/setters
}
