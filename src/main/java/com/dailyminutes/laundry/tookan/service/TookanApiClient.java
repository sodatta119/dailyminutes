/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 24/08/25
 */
package com.dailyminutes.laundry.tookan.service;

import com.dailyminutes.laundry.tookan.config.TookanProperties;
import com.dailyminutes.laundry.tookan.dto.CreateTookanTaskRequest;
import com.dailyminutes.laundry.tookan.dto.TookanTaskResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class TookanApiClient {

    private static final Logger logger = LoggerFactory.getLogger(TookanApiClient.class);
    private final TookanProperties properties;
    private final WebClient webClient = WebClient.create();

    public void createTaskInTookan(CreateTookanTaskRequest request) {
        logger.info("Sending task creation request to Tookan for orderId: {}", request.orderId());

        webClient.post()
                .uri(properties.getApiUrl())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TookanTaskResponse.class)
                .doOnSuccess(response -> {
                    if (response.status() == 200) {
                        logger.info("Successfully created task in Tookan. Job ID: {}", response.jobId());
                    } else {
                        logger.error("Failed to create task in Tookan. Status: {}, Message: {}", response.status(), response.message());
                    }
                })
                .doOnError(error -> {
                    logger.error("Error calling Tookan API", error);
                })
                .subscribe(); // This remains non-blocking
    }
}