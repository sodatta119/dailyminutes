/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/09/25
 */
package com.dailyminutes.laundry.integration.tookan.mapper;

import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.integration.tookan.dto.task.TookanCreateTaskRequest;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.store.domain.event.LogisticsStoreGeofenceResponseEvent;
import org.springframework.stereotype.Component;

@Component
public class TookanTaskMapper {

    public TookanCreateTaskRequest toTookanCreateTaskRequest(LogisticsStoreGeofenceResponseEvent event) {
        CustomerInfoResponseEvent customerEvent= (CustomerInfoResponseEvent) event.customerEvent();
        OrderCreatedEvent orderEvent= (OrderCreatedEvent) event.orderEvent();
        return new TookanCreateTaskRequest(
                // map fields
                "api_key",
                orderEvent.orderId()+"",
                "",
                customerEvent.currentAddress(),
                customerEvent.currentAddressLatitude(),
                customerEvent.currentAddressLongitude(),
                customerEvent.currentAddress(),
                customerEvent.currentAddressLatitude(),
                customerEvent.currentAddressLongitude(),
                customerEvent.customerEmail(),
                customerEvent.customerName(),
                customerEvent.customerPhoneNumber(),
                "Laundry_pickup",
                null,
                null,
                null
        );
    }
}
