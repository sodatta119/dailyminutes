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

/**
 * The type Store event listener.
 */
@Component
@RequiredArgsConstructor
public class StoreEventListener {

    private final StoreRepository storeRepository;
    private final ApplicationEventPublisher events;

    /**
     * On store info requested.
     *
     * @param event the event
     */
    @ApplicationModuleListener
    public void onStoreInfoRequested(StoreInfoRequestEvent event) {
        storeRepository.findById(event.storeId()).ifPresent(store -> {
            events.publishEvent(new StoreInfoResponseEvent(
                    store.getId(),
                    store.getName(),
                    store.getAddress(),
                    store.getContactNumber(),
                    store.getEmail(),
                    event.originalEvent() // Pass the catalogId back for correlation
            ));
        });
    }
}
