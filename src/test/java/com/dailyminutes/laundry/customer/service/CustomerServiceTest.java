package com.dailyminutes.laundry.customer.service;


import com.dailyminutes.laundry.customer.domain.event.CustomerCreatedEvent;
import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.customer.dto.CreateCustomerRequest;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Customer service test.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
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
}
