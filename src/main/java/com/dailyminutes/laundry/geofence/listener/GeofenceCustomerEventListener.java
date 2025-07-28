/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.geofence.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerAddressAddedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerAddressRemovedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerAddressUpdatedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerDeletedEvent;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceCustomerSummaryEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceCustomerSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeofenceCustomerEventListener {

    private final GeofenceCustomerSummaryRepository summaryRepository;

    private void createOrUpdateSummary(Long customerId, Long geofenceId, String name, String phone) {
        if (geofenceId == null) {
            return; // Don't create a summary if the address is not in a geofence
        }
        // A customer can only be in one geofence summary, so we update or create.
        GeofenceCustomerSummaryEntity summary = summaryRepository.findByCustomerId(customerId)
                .orElse(new GeofenceCustomerSummaryEntity());

        summary.setCustomerId(customerId);
        summary.setGeofenceId(geofenceId);
        summary.setCustomerName(name);
        summary.setCustomerPhoneNumber(phone);

        summaryRepository.save(summary);
    }

    @ApplicationModuleListener
    public void onCustomerAddressAdded(CustomerAddressAddedEvent event) {
        createOrUpdateSummary(event.customerId(), event.geofenceId(), event.customerName(), event.customerPhoneNumber());
    }

    @ApplicationModuleListener
    public void onCustomerAddressUpdated(CustomerAddressUpdatedEvent event) {
        createOrUpdateSummary(event.customerId(), event.geofenceId(), event.customerName(), event.customerPhoneNumber());
    }

    @ApplicationModuleListener
    public void onCustomerAddressRemoved(CustomerAddressRemovedEvent event) {
        // If an address is removed, we remove the corresponding summary
        summaryRepository.findByCustomerId(event.customerId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }

    @ApplicationModuleListener
    public void onCustomerDeleted(CustomerDeletedEvent event) {
        // If a customer is deleted, we also remove their summary
        summaryRepository.findByCustomerId(event.customerId()).ifPresent(summary ->
                summaryRepository.deleteById(summary.getId())
        );
    }
}