/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/09/25
 */
package com.dailyminutes.laundry.integration.tookan.listener;


import com.dailyminutes.laundry.customer.domain.event.CustomerInfoResponseEvent;
import com.dailyminutes.laundry.integration.tookan.client.TookanSyncClient;
import com.dailyminutes.laundry.integration.tookan.mapper.TookanTaskMapper;
import com.dailyminutes.laundry.order.domain.event.LogisticsOrderEvent;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.store.domain.event.LogisticsStoreGeofenceRequestEvent;
import com.dailyminutes.laundry.store.domain.event.LogisticsStoreGeofenceResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogisticsOrderListener {


    private final TookanSyncClient tookanClient;
    private final ApplicationEventPublisher events;
    private final TookanTaskMapper mapper;

    @ApplicationModuleListener
    public void onOrderCreated(LogisticsOrderEvent event) {
        try {
            log.info("Received OrderCreatedEvent for orderId={}", event.orderId());
            if(event.originalEvent() instanceof CustomerInfoResponseEvent)
            {
                CustomerInfoResponseEvent customerEvent= (CustomerInfoResponseEvent) event.originalEvent();
                if(customerEvent.originalEvent() instanceof OrderCreatedEvent)
                {
                    OrderCreatedEvent orderEvent= (OrderCreatedEvent) customerEvent.originalEvent();
                    LogisticsStoreGeofenceRequestEvent storeEvent=new LogisticsStoreGeofenceRequestEvent(customerEvent.currentAddressGeofenceId(), customerEvent, orderEvent);
                    publish(storeEvent);
                }
            }

//            // 1) Map internal order → Tookan request
//            var createTaskReq = mapper.toTookanCreateTaskRequest(event);
//
//            // 2) Call Tookan API
//            var tookanResp = tookanClient.createTask(createTaskReq);
//
//            if (tookanResp.status() != 200) {
//                throw new IllegalStateException("Failed to create Tookan task: " + tookanResp.message());
//            }
//
//            var job = tookanResp.data(); // tookan response payload
//
//            log.info("Created Tookan task {} for order {}", job.jobId(), event.orderId());
//
//            // 3) Publish TookanTaskCreatedEvent
//            events.publishEvent(new TookanTaskCreatedEvent(
//                    event.orderId(),
//                    String.valueOf(job.jobId()),    // external Tookan job_id
//                    job.orderId(),                  // if Tookan assigns its own order ref
//                    LocalDateTime.now()
//            ));

        } catch (Exception e) {
            log.error("Error while creating Tookan task for order {}", event.orderId(), e);
            // optionally raise a failure event or retry
        }
    }

    @Transactional
    private void publish(LogisticsStoreGeofenceRequestEvent event)
    {
        events.publishEvent(event);
    }

    @ApplicationModuleListener
    public void onResponseFromStore(LogisticsStoreGeofenceResponseEvent event) {
        OrderCreatedEvent orderEvent= (OrderCreatedEvent) event.orderEvent();
        CustomerInfoResponseEvent customerEvent= (CustomerInfoResponseEvent) event.customerEvent();
        String storeAddress=event.storeAddress();
        String storeLat=event.storeLatitude();
        String storeLong=event.storeLongitude();
        Long storeId=event.storeId();
        String storeName=event.storeName();
    }


}
