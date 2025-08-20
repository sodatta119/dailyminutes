package com.dailyminutes.laundry.geofence.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerAddressAddedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerAddressRemovedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerAddressUpdatedEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerDeletedEvent;
import com.dailyminutes.laundry.customer.domain.model.AddressType;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceCustomerSummaryEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceCustomerSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * The type Geofence customer event listener test.
 */
@ExtendWith(MockitoExtension.class)
class GeofenceCustomerEventListenerTest {

    @Mock
    private GeofenceCustomerSummaryRepository summaryRepository;

    @InjectMocks
    private GeofenceCustomerEventListener listener;

    /**
     * On customer address added should create summary.
     */
    @Test
    void onCustomerAddressAdded_shouldCreateSummary() {
        // Given: An event for a new customer address with a geofence
        CustomerAddressAddedEvent event = new CustomerAddressAddedEvent(
                1L, 10L, "John Doe", "555-0001", AddressType.HOME,
                true, "123 Main St", "Anytown", "12345", 100L
        );
        when(summaryRepository.findByCustomerId(10L)).thenReturn(Optional.empty());
        ArgumentCaptor<GeofenceCustomerSummaryEntity> captor = ArgumentCaptor.forClass(GeofenceCustomerSummaryEntity.class);

        // When: The listener handles the event
        listener.onCustomerAddressAdded(event);

        // Then: A new summary entity is saved
        verify(summaryRepository).save(captor.capture());
        GeofenceCustomerSummaryEntity savedSummary = captor.getValue();
        assertThat(savedSummary.getCustomerId()).isEqualTo(10L);
        assertThat(savedSummary.getGeofenceId()).isEqualTo(100L);
        assertThat(savedSummary.getCustomerName()).isEqualTo("John Doe");
        assertThat(savedSummary.getCustomerPhoneNumber()).isEqualTo("555-0001");
    }

    /**
     * On customer address updated should update summary.
     */
    @Test
    void onCustomerAddressUpdated_shouldUpdateSummary() {
        // Given: An event for an updated customer address
        CustomerAddressUpdatedEvent event = new CustomerAddressUpdatedEvent(
                1L, 10L, "John Doe Sr.", "555-0002", AddressType.HOME,
                true, "456 New St", "Newville", "54321", 200L
        );

        // And an existing summary for that customer
        GeofenceCustomerSummaryEntity existingSummary = new GeofenceCustomerSummaryEntity(
                5L, 10L, 100L, "John Doe", "555-0001"
        );
        when(summaryRepository.findByCustomerId(10L)).thenReturn(Optional.of(existingSummary));
        ArgumentCaptor<GeofenceCustomerSummaryEntity> captor = ArgumentCaptor.forClass(GeofenceCustomerSummaryEntity.class);

        // When: The listener handles the event
        listener.onCustomerAddressUpdated(event);

        // Then: The existing summary is updated and saved
        verify(summaryRepository).save(captor.capture());
        GeofenceCustomerSummaryEntity updatedSummary = captor.getValue();
        assertThat(updatedSummary.getId()).isEqualTo(5L); // Same record
        assertThat(updatedSummary.getGeofenceId()).isEqualTo(200L); // New geofence
        assertThat(updatedSummary.getCustomerName()).isEqualTo("John Doe Sr.");
        assertThat(updatedSummary.getCustomerPhoneNumber()).isEqualTo("555-0002");
    }

    /**
     * On customer address removed should delete summary.
     */
    @Test
    void onCustomerAddressRemoved_shouldDeleteSummary() {
        // Given: An event for a removed customer address
        CustomerAddressRemovedEvent event = new CustomerAddressRemovedEvent(1L, 10L);
        GeofenceCustomerSummaryEntity summaryToDelete = new GeofenceCustomerSummaryEntity(
                5L, 10L, 100L, "John Doe", "555-0001"
        );
        when(summaryRepository.findByCustomerId(10L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the event
        listener.onCustomerAddressRemoved(event);

        // Then: The summary is deleted
        verify(summaryRepository).deleteById(5L);
    }

    /**
     * On customer deleted should delete summary.
     */
    @Test
    void onCustomerDeleted_shouldDeleteSummary() {
        // Given: An event for a deleted customer
        CustomerDeletedEvent event = new CustomerDeletedEvent(10L);
        GeofenceCustomerSummaryEntity summaryToDelete = new GeofenceCustomerSummaryEntity(
                5L, 10L, 100L, "John Doe", "555-0001"
        );
        when(summaryRepository.findByCustomerId(10L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the event
        listener.onCustomerDeleted(event);

        // Then: The summary is deleted
        verify(summaryRepository).deleteById(5L);
    }
}