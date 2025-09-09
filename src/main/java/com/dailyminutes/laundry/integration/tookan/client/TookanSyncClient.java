/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.tookan.client;

import com.dailyminutes.laundry.integration.tookan.config.TookanProperties;
import com.dailyminutes.laundry.integration.tookan.dto.agent.FleetRecord;
import com.dailyminutes.laundry.integration.tookan.dto.agent.TookanFleetsResponse;
import com.dailyminutes.laundry.integration.tookan.dto.geofence.*;
import com.dailyminutes.laundry.integration.tookan.dto.team.TookanTeamRecord;
import com.dailyminutes.laundry.integration.tookan.dto.team.TookanTeamsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * The type Tookan sync client.
 */
// com.dailyminutes.laundry.integration.tookan.SimpleTookanClient.java
@Component
@Slf4j
public class TookanSyncClient {
    private final WebClient tookanClient;
    private final TookanProperties props;
    private final com.fasterxml.jackson.databind.ObjectMapper mapper;

    /**
     * Instantiates a new Tookan sync client.
     *
     * @param tookanClient the tookan client
     * @param props        the props
     * @param mapper       the mapper
     */
    public TookanSyncClient(WebClient tookanClient, TookanProperties props,
                            com.fasterxml.jackson.databind.ObjectMapper mapper) {
        this.tookanClient = tookanClient;
        this.props = props;
        this.mapper = mapper; // Spring Boot's mapper has JavaTimeModule registered
    }

    /**
     * List teams list.
     *
     * @return the list
     */
    public List<TookanTeamRecord> listTeams() {
        return tookanClient.post()
                .uri("/v2/view_all_team_only")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("api_key", props.apiKey())))
                .exchangeToMono(resp -> {
                    var isJson = resp.headers().contentType()
                            .map(ct -> ct.includes(MediaType.APPLICATION_JSON) || "json".equalsIgnoreCase(ct.getSubtypeSuffix()))
                            .orElse(false);

                    if (isJson) {
                        return resp.bodyToMono(TookanTeamsResponse.class)
                                .map(r -> {
                                    if (r.status() != 200) {
                                        throw new IllegalStateException("Tookan error: " + r.status() + " " + r.message());
                                    }
                                    return r.data() == null ? List.<TookanTeamRecord>of() : r.data();
                                });
                    }

                    // Fallback to String and parse
                    return resp.bodyToMono(String.class).map(body -> {
                        if (body != null && body.trim().startsWith("<")) {
                            throw new IllegalStateException(
                                    "Tookan returned HTML instead of JSON. First 200 chars: "
                                            + body.substring(0, Math.min(200, body.length())));
                        }
                        try {
                            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                            var parsed = mapper.readValue(body, TookanTeamsResponse.class);
                            if (parsed.status() != 200) {
                                throw new IllegalStateException("Tookan error: " + parsed.status() + " " + parsed.message());
                            }
                            return parsed.data() == null ? List.<TookanTeamRecord>of() : parsed.data();
                        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                            throw new IllegalStateException("Failed to parse Tookan JSON. Body starts: "
                                    + (body == null ? "null" : body.substring(0, Math.min(200, body.length()))), e);
                        }
                    });
                })
                .block();
    }


    /**
     * List geofences list.
     *
     * @return the list
     */
    public List<GeofenceRecord> listGeofences() {
        String body = tookanClient.post()
                .uri("/v2/view_regions") // or "/v2/view_all_region" if that’s your actual endpoint
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "api_key", props.apiKey(),
                        "user_id", 1696212L
                ))
                .retrieve()
                .onStatus(s -> s.value() >= 400, r -> r.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(b -> new IllegalStateException("Tookan HTTP " + r.statusCode().value() + ": " + b)))
                .bodyToMono(String.class)
                .block();

        if (body == null || body.isBlank()) {
            throw new IllegalStateException("Empty response from Tookan /v2/view_regions");
        }
        // If a WAF/error page returns HTML, surface a clear error
        if (body.trim().startsWith("<")) {
            throw new IllegalStateException("Tookan returned HTML instead of JSON. First 200 chars: "
                    + body.substring(0, Math.min(200, body.length())));
        }

        try {
            var resp = mapper.readValue(body, TookanGeofencesResponse.class);
            if (resp.status() != 200) {
                throw new IllegalStateException("Tookan error: " + resp.status() + " " + resp.message());
            }
            return resp.data() == null ? List.of() : resp.data();
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse Tookan regions JSON. Body starts: "
                    + body.substring(0, Math.min(200, body.length())), e);
        }
    }

    /**
     * List agents list.
     *
     * @return the list
     */
    public List<FleetRecord> listAgents() {
        String body = tookanClient.post()
                .uri("/v2/get_all_fleets")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("api_key", props.apiKey(),"include_team_id",1))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (body == null || body.isBlank()) {
            throw new IllegalStateException("Empty response from Tookan /v2/get_all_fleets");
        }
        if (body.trim().startsWith("<")) {
            throw new IllegalStateException("Tookan returned HTML for fleets. First 200 chars: "
                    + body.substring(0, Math.min(200, body.length())));
        }
        try {
            TookanFleetsResponse resp = mapper.readValue(body, TookanFleetsResponse.class);
            if (resp.status() != 200) {
                throw new IllegalStateException("Tookan fleets error: " + resp.status() + " " + resp.message());
            }
            return resp.data() == null ? List.of() : resp.data();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse Tookan fleets JSON. Body starts: "
                    + body.substring(0, Math.min(200, body.length())), e);
        }
    }

    /**
     * Finds Tookan regions that contain any of the provided points.
     *
     * @param points list of coordinates (latitude := x, longitude := y)
     * @return list of TookanRegion found (empty list if none)
     */
    public List<GeofenceRegion> findRegionFromPoints(List<RegionPoint> points) {
        var requestBody = Map.of(
                "api_key", props.apiKey(),
                "points", points.stream()
                        .map(p -> Map.of("latitude", p.x(), "longitude", p.y()))
                        .toList()
        );

        return tookanClient.post()
                .uri("/v2/find_region_from_points")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .exchangeToMono(resp -> {
                    var isJson = resp.headers().contentType()
                            .map(ct -> ct.includes(MediaType.APPLICATION_JSON))
                            .orElse(false);

                    if (isJson) {
                        return resp.bodyToMono(FindRegionResponse.class)
                                .map(r -> {
                                    if (r.status() != 200) {
                                        throw new IllegalStateException("Tookan error: " + r.status() + " " + r.message());
                                    }
                                    return r.data() == null ? List.<GeofenceRegion>of() : r.data();
                                });
                    }

                    // fallback: read as String and parse
                    return resp.bodyToMono(String.class).map(body -> {
                        if (body != null && body.trim().startsWith("<")) {
                            throw new IllegalStateException("Tookan returned HTML instead of JSON. First 200 chars: "
                                    + body.substring(0, Math.min(200, body.length())));
                        }
                        try {
                            var parsed = mapper.readValue(body, FindRegionResponse.class);
                            if (parsed.status() != 200) {
                                throw new IllegalStateException("Tookan error: " + parsed.status() + " " + parsed.message());
                            }
                            return parsed.data() == null ? List.<GeofenceRegion>of() : parsed.data();
                        } catch (JsonProcessingException e) {
                            throw new IllegalStateException("Failed to parse Tookan JSON. Body starts: "
                                    + (body == null ? "null" : body.substring(0, Math.min(200, body.length()))), e);
                        }
                    });
                })
                .block(); // blocking call — keep it consistent with existing client style
    }


}
