/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 24/08/25
 */
package com.dailyminutes.laundry.tookan.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "integration.tookan")
@Validated
@Getter
@Setter
public class TookanProperties {

    @NotBlank(message = "Tookan API URL is required")
    private String apiUrl = "https://api.tookanapp.com/v2/create_task";

    @NotBlank(message = "Tookan API key is required")
    private String apiKey;
}