package com.dailyminutes.laundry.catalog.service;


import com.dailyminutes.laundry.customer.domain.event.*;
import com.dailyminutes.laundry.customer.domain.model.AddressType;
import com.dailyminutes.laundry.customer.domain.model.CustomerAddressEntity;
import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.customer.dto.CreateCustomerAddressRequest;
import com.dailyminutes.laundry.customer.dto.CreateCustomerRequest;
import com.dailyminutes.laundry.customer.dto.UpdateCustomerAddressRequest;
import com.dailyminutes.laundry.customer.dto.UpdateCustomerRequest;
import com.dailyminutes.laundry.customer.repository.CustomerAddressRepository;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import com.dailyminutes.laundry.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The type Customer service test.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerAddressRepository addressRepository;
    @Mock
    private ApplicationEventPublisher events;
    @InjectMocks
    private CustomerService customerService;

    /**
     * Create customer should create and publish event.
     */
    @Test
    void createCustomer_shouldCreateAndPublishEvent() {
        CreateCustomerRequest request = new CreateCustomerRequest("sub1", "1234567890", "Test", "test@test.com","IST", LocalDateTime.now());
        CustomerEntity customer = new CustomerEntity(1L, "sub1", "1234567890", "Test", "test@test.com","IST", LocalDateTime.now());
        when(customerRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(customer);

        customerService.createCustomer(request);

        verify(events).publishEvent(any(CustomerCreatedEvent.class));
    }

    /**
     * Update customer should update and publish event.
     */
    @Test
    void updateCustomer_shouldUpdateAndPublishEvent() {
        UpdateCustomerRequest request = new UpdateCustomerRequest(1L, "sub1", "1234567890", "Test Updated", "test.updated@test.com");
        CustomerEntity customer = new CustomerEntity(1L, "sub1", "1234567890", "Test", "test@test.com","IST", LocalDateTime.now());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        customerService.updateCustomer(request);

        verify(events).publishEvent(any(CustomerUpdatedEvent.class));
    }

    /**
     * Delete customer should delete and publish event.
     */
    @Test
    void deleteCustomer_shouldDeleteAndPublishEvent() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        customerService.deleteCustomer(1L);

        verify(events).publishEvent(any(CustomerDeletedEvent.class));
    }

    /**
     * Add address should add and publish event.
     */
    @Test
    void addAddress_shouldAddAndPublishEvent() {
        CreateCustomerAddressRequest request = new CreateCustomerAddressRequest(1L, AddressType.HOME, true, null, "123 Main St", null, "Anytown", null, "12345", null, null, null, null);
        CustomerAddressEntity address = new CustomerAddressEntity(1L, 1L, AddressType.HOME, true, null, "123 Main St", null, "Anytown", null, "12345", null, null, null, null);
        CustomerEntity customer = new CustomerEntity(1L, "test", "9999", "testcustomer", "testemail","IST", LocalDateTime.now());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepository.save(any())).thenReturn(address);

        customerService.addAddress(request);

        verify(events).publishEvent(any(CustomerAddressAddedEvent.class));
    }

    /**
     * Update address should update and publish event.
     */
    @Test
    void updateAddress_shouldUpdateAndPublishEvent() {
        UpdateCustomerAddressRequest request = new UpdateCustomerAddressRequest(1L, 1L, AddressType.HOME, true, null, "456 Main St", null, "Anytown", null, "12345", null, null, null, null);
        CustomerAddressEntity address = new CustomerAddressEntity(1L, 1L, AddressType.HOME, true, null, "123 Main St", null, "Anytown", null, "12345", null, null, null, null);
        CustomerEntity customer = new CustomerEntity(1L, "test", "9999", "testcustomer", "testemail","IST", LocalDateTime.now());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any())).thenReturn(address);

        customerService.updateAddress(request);

        verify(events).publishEvent(any(CustomerAddressUpdatedEvent.class));
    }

    /**
     * Remove address should remove and publish event.
     */
    @Test
    void removeAddress_shouldRemoveAndPublishEvent() {
        CustomerAddressEntity address = new CustomerAddressEntity(1L, 1L, AddressType.HOME, true, null, "123 Main St", null, "Anytown", null, "12345", null, null, null, null);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        doNothing().when(addressRepository).deleteById(1L);

        customerService.removeAddress(1L);

        verify(events).publishEvent(any(CustomerAddressRemovedEvent.class));
    }

    /**
     * Update customer should throw exception when customer not found.
     */
    @Test
    void updateCustomer_shouldThrowException_whenCustomerNotFound() {
        UpdateCustomerRequest request = new UpdateCustomerRequest(1L, "sub1", "1234567890", "Test Updated", "test.updated@test.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(request));
    }
}
