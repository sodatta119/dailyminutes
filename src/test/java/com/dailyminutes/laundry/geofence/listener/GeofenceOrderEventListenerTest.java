package com.dailyminutes.laundry.geofence.listener;

import com.dailyminutes.laundry.customer.domain.event.CustomerAddressInfoRequestEvent;
import com.dailyminutes.laundry.customer.domain.event.CustomerAddressInfoResponseEvent;
import com.dailyminutes.laundry.geofence.domain.model.GeofenceOrderSummaryEntity;
import com.dailyminutes.laundry.geofence.repository.GeofenceOrderSummaryRepository;
import com.dailyminutes.laundry.order.domain.event.OrderCreatedEvent;
import com.dailyminutes.laundry.order.domain.event.OrderDeletedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeofenceOrderEventListenerTest {

    @Mock
    private GeofenceOrderSummaryRepository summaryRepository;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private GeofenceOrderEventListener listener;

    @Test
    void onOrderCreated_shouldRequestCustomerGeofenceInfo() {
        // Given: An order creation event
        OrderCreatedEvent event = new OrderCreatedEvent(
                101L, 201L, 301L, "PENDING",
                new BigDecimal("50.00"), LocalDateTime.now(), Collections.emptyList()
        );
        ArgumentCaptor<CustomerAddressInfoRequestEvent> requestCaptor = ArgumentCaptor.forClass(CustomerAddressInfoRequestEvent.class);

        // When: The listener handles the order creation
        listener.onOrderPlacedInGeofence(event);

        // Then: It should publish a new event to request the customer's geofence info
        verify(events).publishEvent(requestCaptor.capture());
        CustomerAddressInfoRequestEvent request = requestCaptor.getValue();

        assertThat(request.customerId()).isEqualTo(201L);
        assertThat(request.originalEvent()).isEqualTo(event);
    }

    @Test
    void onCustomerAddressReceived_shouldCreateOrderSummary() {
        // Given: A response event from the customer module with a valid geofenceId
        OrderCreatedEvent originalEvent = new OrderCreatedEvent(
                101L, 201L, 301L, "PENDING",
                new BigDecimal("50.00"), LocalDateTime.now(), Collections.emptyList()
        );
        CustomerAddressInfoResponseEvent event = new CustomerAddressInfoResponseEvent(201L, "test-address", "test-lat", "testLong", 999L, originalEvent);
        ArgumentCaptor<GeofenceOrderSummaryEntity> summaryCaptor = ArgumentCaptor.forClass(GeofenceOrderSummaryEntity.class);

        // When: The listener handles the response
        listener.onCustomerAddressReceived(event);

        // Then: It should save a new summary entity with all the combined data
        verify(summaryRepository).save(summaryCaptor.capture());
        GeofenceOrderSummaryEntity summary = summaryCaptor.getValue();

        assertThat(summary.getOrderId()).isEqualTo(101L);
        assertThat(summary.getGeofenceId()).isEqualTo(999L);
        assertThat(summary.getCustomerId()).isEqualTo(201L);
        assertThat(summary.getStoreId()).isEqualTo(301L);
        assertThat(summary.getStatus()).isEqualTo("PENDING");
    }

    @Test
    void onOrderDeleted_shouldDeleteSummary() {
        // Given: An order deletion event
        OrderDeletedEvent event = new OrderDeletedEvent(101L);
        GeofenceOrderSummaryEntity summaryToDelete = new GeofenceOrderSummaryEntity();
        when(summaryRepository.findByOrderId(101L)).thenReturn(Optional.of(summaryToDelete));

        // When: The listener handles the deletion
        listener.onOrderDeleted(event);

        // Then: The corresponding summary should be deleted
        verify(summaryRepository).deleteById(summaryToDelete.getId());
    }
}