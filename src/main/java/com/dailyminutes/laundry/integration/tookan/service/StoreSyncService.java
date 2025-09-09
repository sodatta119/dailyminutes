/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 30/08/25
 */
package com.dailyminutes.laundry.integration.tookan.service;


import com.dailyminutes.laundry.store.domain.event.StoreSyncEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Team sync service.
 */
@Service
@RequiredArgsConstructor
public class StoreSyncService {

    private final ApplicationEventPublisher events;

    /**
     * Sync teams.
     */
    @Transactional
    public void syncStores() {
        StoreSyncEvent.StoreSyncPayload powaiStore=new StoreSyncEvent.StoreSyncPayload("100","Powai","601, Pach Shrishti Complex, Powai, Mumbai, Maharashtra 400076", "919594599719", "sodatta@gmail.com",true,"19.1144751903063","72.90463783425005", List.of(36841L));
        StoreSyncEvent.StoreSyncPayload bellandurStore=new StoreSyncEvent.StoreSyncPayload("101","Bellandur","Daily Minutes Laundry, Near Greenfield Apartments, NR Layout Rd, Bellandur, Bengaluru, Karnataka 560103", "919594599719", "sodatta@gmail.com",true,"12.922206999801643","77.67725327116071", List.of(36847L, 37241L, 37242L, 37244L, 37245L, 37246L, 37247L, 37956L));
        StoreSyncEvent.StoreSyncPayload marathahalliStore=new StoreSyncEvent.StoreSyncPayload("102","Marathahalli","Munnekolal DailyMinutes Laundry, opp-saptagiri homes, 136, Shirdi Sai Nagar, Munnekollal, Bengaluru, Karnataka 560037", "919594599719", "sodatta@gmail.com",true,"12.948892063508168","77.71452618730584", List.of(36841L, 37957L, 37862L, 38018L));
        events.publishEvent(new StoreSyncEvent(List.of(powaiStore, bellandurStore, marathahalliStore)));
    }
}
