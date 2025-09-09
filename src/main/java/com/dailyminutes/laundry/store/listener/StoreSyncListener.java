/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.store.listener;

import com.dailyminutes.laundry.geofence.domain.event.GeofenceLookupRequestEvent;
import com.dailyminutes.laundry.geofence.domain.event.GeofenceLookupResponseEvent;
import com.dailyminutes.laundry.store.domain.event.StoreCreatedEvent;
import com.dailyminutes.laundry.store.domain.event.StoreSyncEvent;
import com.dailyminutes.laundry.store.dto.CreateStoreRequest;
import com.dailyminutes.laundry.store.dto.StoreResponse;
import com.dailyminutes.laundry.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Team sync listener.
 */
@Component
@RequiredArgsConstructor
class StoreSyncListener {

    private final StoreService storeService; // your domain service
    private final ApplicationEventPublisher events;

    /**
     * On teams sync event.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onStoreSyncEvent(StoreSyncEvent event) {
        event.stores().forEach(store -> {
            StoreResponse response=storeService.createStore(new CreateStoreRequest(store.name(),store.address(),store.contact(),store.email(),null,store.latitude(),store.longitude()));
            if(response.id()!=null)
            {
                store.serviceGeofences().forEach(geo->{
                    GeofenceLookupRequestEvent geoEvent=new GeofenceLookupRequestEvent(geo, new StoreCreatedEvent(response.id(), response.name(), response.managerId()));
                    publish(geoEvent);
                });
            }
        });
    }

    @Transactional
    private void publish(GeofenceLookupRequestEvent event){
        events.publishEvent(event);
    }

    @ApplicationModuleListener
    public void onGeofenceLookupResponse(GeofenceLookupResponseEvent event) {
        if(event.originalEvent() instanceof StoreCreatedEvent)
        {
            StoreCreatedEvent storeEvent = (StoreCreatedEvent) event.originalEvent();
            Long geofenceId=event.internalGeofenceId();
            storeService.assignGeofenceToStore(storeEvent.storeId(), geofenceId);
        }
    }
}
