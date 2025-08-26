/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.tookan.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

// com.dailyminutes.laundry.integration.tookan.SimpleTookanClient.java
@Component
@RequiredArgsConstructor
public class SimpleTookanClient {
    private final WebClient tookanClient;

    public List<Map<String,Object>> listTeams() {
        return tookanClient.get().uri("/v2/teams")
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<Map<String,Object>>>(){}).block();
    }

    public List<Map<String,Object>> listAgents() {
        return tookanClient.get().uri("/v2/agents")
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<Map<String,Object>>>(){}).block();
    }

    public List<Map<String,Object>> listGeofences() {
        return tookanClient.get().uri("/v2/geofences")
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<Map<String,Object>>>(){}).block();
    }
}
