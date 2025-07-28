/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.store.listener;

import com.dailyminutes.laundry.store.domain.event.StoreInfoResponseEvent;
import com.dailyminutes.laundry.store.domain.event.StoreInfoRequestEvent;
import com.dailyminutes.laundry.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreEventListener {

    private final StoreRepository storeRepository;
    private final ApplicationEventPublisher events;

    @ApplicationModuleListener
    public void onStoreInfoRequested(StoreInfoRequestEvent event) {
        storeRepository.findById(event.storeId()).ifPresent(store -> {
            events.publishEvent(new StoreInfoResponseEvent(
                    store.getId(),
                    store.getName(),
                    event.originalEvent() // Pass the catalogId back for correlation
            ));
        });
    }
}
