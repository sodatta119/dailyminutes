/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 31/08/25
 */
package com.dailyminutes.laundry.agent.domain.event;


import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Agent sync event.
 */
public class AgentSyncEvent {

    private final List<AgentSyncPayload> payloads;

    /**
     * Instantiates a new Agent sync event.
     *
     * @param payloads the payloads
     */
    public AgentSyncEvent(List<AgentSyncPayload> payloads) {
        this.payloads = payloads;
    }

    /**
     * Gets payloads.
     *
     * @return the payloads
     */
    public List<AgentSyncPayload> getPayloads() {
        return payloads;
    }

    /**
     * The type Agent sync payload.
     */
    public record AgentSyncPayload(
            String externalId,        // fleet_id from Tookan
            String name,
            String email,
            String phoneNumber,
            Long teamId,
            Double latitude,
            Double longitude,
            int active,               // is_active from Tookan
            int available,            // is_available from Tookan
            String fleetThumbImage,
            LocalDateTime externalSyncAt
    ) {}
}
