/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/08/25
 */
package com.dailyminutes.laundry.integration.manychat.client;

import com.dailyminutes.laundry.integration.manychat.dto.customer.ManychatCustomerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The type Manychat sync client.
 */
// com.dailyminutes.laundry.integration.tookan.SimpleTookanClient.java
@Component
@Slf4j
public class ManychatSyncClient {
    private final WebClient manyChatClient;
    private final ObjectMapper mapper;

    /**
     * Instantiates a new Manychat sync client.
     *
     * @param manyChatClient the many chat client
     * @param mapper         the mapper
     */
    public ManychatSyncClient(WebClient manyChatClient, ObjectMapper mapper) {
        this.manyChatClient = manyChatClient;
        this.mapper = mapper;
    }

    /**
     * Find subscribers by custom field manychat customer response.
     *
     * @param fieldId the field id
     * @param value   the value
     * @return the manychat customer response
     */
    public ManychatCustomerResponse findSubscribersByCustomField(long fieldId, String value) {
        String raw = manyChatClient.get()
                .uri(uri -> uri
                        .path("/fb/subscriber/findByCustomField")
                        .queryParam("field_id", fieldId)
                        .queryParam("field_value", value)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (raw == null || raw.isBlank()) {
            throw new IllegalStateException("Empty response from ManyChat findByCustomField");
        }
        if (raw.trim().startsWith("<")) {
            throw new IllegalStateException("ManyChat returned HTML. First 200 chars: "
                    + raw.substring(0, Math.min(200, raw.length())));
        }
        try {
            return mapper.readValue(raw, ManychatCustomerResponse.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse ManyChat JSON. Body starts: "
                    + raw.substring(0, Math.min(200, raw.length())), e);
        }
    }

}
