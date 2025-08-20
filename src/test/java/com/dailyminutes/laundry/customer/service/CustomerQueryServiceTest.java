package com.dailyminutes.laundry.customer.service;


import com.dailyminutes.laundry.customer.domain.model.CustomerEntity;
import com.dailyminutes.laundry.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * The type Customer query service test.
 */
@ExtendWith(MockitoExtension.class)
class CustomerQueryServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerQueryService customerQueryService;

    /**
     * Find customer by id should return customer.
     */
    @Test
    void findCustomerById_shouldReturnCustomer() {
        CustomerEntity customer = new CustomerEntity(1L, "sub1", "1234567890", "Test", "test@test.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThat(customerQueryService.findCustomerById(1L)).isPresent();
    }
}
