/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.integration.manychat.service;


import com.dailyminutes.laundry.customer.domain.event.CustomerSyncEvent;
import com.dailyminutes.laundry.integration.manychat.client.ManychatSyncClient;
import com.dailyminutes.laundry.integration.manychat.dto.CustomField;
import com.dailyminutes.laundry.integration.manychat.dto.Subscriber;
import com.dailyminutes.laundry.integration.manychat.dto.customer.ManychatCustomerResponse;
import com.dailyminutes.laundry.integration.tookan.event.CustomerGeofenceRequestEvent;
import com.dailyminutes.laundry.integration.tookan.event.CustomerGeofenceResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * The type Customer sync service.
 */
@Service
@RequiredArgsConstructor
public class CustomerSyncService {

    private final ManychatSyncClient client;
    private final ApplicationEventPublisher events;

    private static final long SESSION_STATE_FIELD_ID = 13154017L;

    /**
     * Sync customers int.
     *
     * @return the int
     */
    @Transactional
    public int syncCustomers() {
        ManychatCustomerResponse resp = client.findSubscribersByCustomField(SESSION_STATE_FIELD_ID, "1");

        if (!"success".equalsIgnoreCase(resp.status())) {
            var err = resp.error() != null
                    ? (resp.error().code() + " " + resp.error().message())
                    : "unknown";
            throw new IllegalStateException("ManyChat error: " + err);
        }

        if (resp.data() == null) return 0;

        resp.data().forEach(this::publishCustomerEvent);
        return resp.data().size();
    }


    private void publishCustomerEvent(Subscriber s) {
        String addressLine = null;
        String latStr = null;
        String lngStr = null;

        if (s.customFields() != null) {
            for (CustomField cf : s.customFields()) {
                switch (cf.name().toLowerCase()) {
                    case "address" -> addressLine = asString(cf.value());
                    case "address_latlong" -> {
                        var latlng = asString(cf.value());
                        if (latlng != null && latlng.contains(",")) {
                            String[] parts = latlng.split(",");
                            if (parts.length >= 2) {
                                latStr = parts[0].trim();
                                lngStr = parts[1].trim();
                            }
                        }
                    }
                }
            }
        }

        LocalDateTime subscribedAt = null;
        if (s.subscribed() != null && !s.subscribed().isBlank()) {
            try {
                var odt = java.time.OffsetDateTime.parse(s.subscribed());
                subscribedAt = odt.toLocalDateTime();
            } catch (Exception ignored) {}
        }

        var payload = new CustomerSyncEvent.CustomerSyncPayload(
                s.id(),
                firstNonBlank(s.name(), s.firstName()),
                s.email(),
                firstNonBlank(s.whatsappPhone(), s.phone()),
                s.timezone(),
                subscribedAt,
                addressLine,
                latStr,
                lngStr,
                null
        );

        //events.publishEvent(new CustomerSyncEvent(payload));
        publishCustomerGeofenceRequestEvent(new CustomerGeofenceRequestEvent(payload.externalId(), new CustomerSyncEvent(payload)));
    }

    @Transactional
    private void publishCustomerGeofenceRequestEvent(CustomerGeofenceRequestEvent event){
        events.publishEvent(event);
    }

    private static String firstNonBlank(String a, String b) {
        return (a != null && !a.isBlank()) ? a : (b != null && !b.isBlank()) ? b : null;
    }
    private static String asString(Object v) { return v == null ? null : String.valueOf(v); }

    @ApplicationModuleListener
    public void onCustomerGeofenceResponse(CustomerGeofenceResponseEvent event) {
        CustomerSyncEvent cEvent= (CustomerSyncEvent) event.originalEvent();
        var oldPayload = cEvent.payload();

        var updatedPayload = new CustomerSyncEvent.CustomerSyncPayload(
                oldPayload.externalId(),
                oldPayload.name(),
                oldPayload.email(),
                oldPayload.phone(),
                oldPayload.timeZone(),
                oldPayload.subscribedAt(),
                oldPayload.addressLine(),
                oldPayload.latitude(),
                oldPayload.longitude(),
                event.geofenceId()
        );

        CustomerSyncEvent updatedEvent = new CustomerSyncEvent(updatedPayload);
        publishCustomerEvent(updatedEvent);
    }

    private void publishCustomerEvent(CustomerSyncEvent event){
        events.publishEvent(event);
    }
}
