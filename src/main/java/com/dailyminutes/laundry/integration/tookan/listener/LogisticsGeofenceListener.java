/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/09/25
 */
package com.dailyminutes.laundry.integration.tookan.listener;


import com.dailyminutes.laundry.customer.domain.event.CustomerSyncEvent;
import com.dailyminutes.laundry.integration.tookan.client.TookanSyncClient;
import com.dailyminutes.laundry.integration.tookan.dto.geofence.RegionPoint;
import com.dailyminutes.laundry.integration.tookan.event.CustomerGeofenceRequestEvent;
import com.dailyminutes.laundry.integration.tookan.event.CustomerGeofenceResponseEvent;
import com.dailyminutes.laundry.integration.tookan.mapper.TookanTaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogisticsGeofenceListener {


    private final TookanSyncClient tookanClient;
    private final ApplicationEventPublisher events;
    private final TookanTaskMapper mapper;

    @ApplicationModuleListener
    public void onCustomerGeofenceRequested(CustomerGeofenceRequestEvent event) {
        try {
            CustomerSyncEvent customerEvent= (CustomerSyncEvent) event.originalEvent();
            if(customerEvent.payload().latitude()!=null && customerEvent.payload().longitude()!=null)
            {
                var points = List.of(new RegionPoint(Double.parseDouble(customerEvent.payload().latitude()), Double.parseDouble(customerEvent.payload().longitude())));
                var regions = tookanClient.findRegionFromPoints(points);
                if (!regions.isEmpty()) {
                    for (var region : regions) {
                        System.out.println("Found region: " + region.regionName() + " id=" + region.regionId());
                        CustomerGeofenceResponseEvent response=new CustomerGeofenceResponseEvent(event.subscriptionId(), region.regionId().toString(), customerEvent);
                        publish(response);
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error while querying Tookan geofence for customer {}", event.subscriptionId(), e);
            // optionally raise a failure event or retry
        }
    }

    @Transactional
    private void publish(CustomerGeofenceResponseEvent event)
    {
        events.publishEvent(event);
    }


}
