package com.dailyminutes.laundry.customer.api;


import com.dailyminutes.laundry.customer.domain.model.AddressType;
import com.dailyminutes.laundry.customer.dto.*;
import com.dailyminutes.laundry.customer.service.CustomerQueryService;
import com.dailyminutes.laundry.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private CustomerQueryService customerQueryService;

    private CustomerResponse customerResponse;
    private UpdateCustomerRequest updateCustomerRequest;
    private CustomerAddressResponse customerAddressResponse;
    private CreateCustomerAddressRequest createCustomerAddressRequest;
    private UpdateCustomerAddressRequest updateCustomerAddressRequest;


    @BeforeEach
    void setUp() {
        customerResponse = new CustomerResponse(1L, "sub1", "1234567890", "Test", "test@test.com");
        updateCustomerRequest = new UpdateCustomerRequest(1L, "sub1", "1234567890", "Test Updated", "test.updated@test.com");
        customerAddressResponse = new CustomerAddressResponse(1L, 1L, AddressType.HOME, true, "Apt 1", "123 Main St", "Main St", "Anytown", "Anystate", "12345", "USA", "0", "0", 1L);
        createCustomerAddressRequest = new CreateCustomerAddressRequest(1L, AddressType.HOME, true, "Apt 1", "123 Main St", "Main St", "Anytown", "Anystate", "12345", "USA", "0", "0", 1L);
        updateCustomerAddressRequest = new UpdateCustomerAddressRequest(1L, 1L, AddressType.HOME, true, "Apt 2", "456 Main St", "Main St", "Anytown", "Anystate", "12345", "USA", "0", "0", 1L);
    }

    @Test
    void createCustomer_shouldReturnCreated() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("sub1", "1234567890", "Test", "test@test.com");
        when(customerService.createCustomer(any())).thenReturn(customerResponse);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getCustomerById_shouldReturnCustomer() throws Exception {
        when(customerQueryService.findCustomerById(1L)).thenReturn(Optional.of(customerResponse));
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void updateCustomer_shouldReturnOk() throws Exception {
        when(customerService.updateCustomer(any())).thenReturn(customerResponse);
        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCustomer_shouldReturnNoContent() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void addAddress_shouldReturnCreated() throws Exception {
        when(customerService.addAddress(any())).thenReturn(customerAddressResponse);
        mockMvc.perform(post("/api/customers/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerAddressRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateAddress_shouldReturnOk() throws Exception {
        when(customerService.updateAddress(any())).thenReturn(customerAddressResponse);
        mockMvc.perform(put("/api/customers/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerAddressRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void removeAddress_shouldReturnNoContent() throws Exception {
        doNothing().when(customerService).removeAddress(1L);
        mockMvc.perform(delete("/api/customers/addresses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAddressesByCustomerId_shouldReturnAddresses() throws Exception {
        when(customerQueryService.findAddressesByCustomerId(1L)).thenReturn(Collections.singletonList(customerAddressResponse));
        mockMvc.perform(get("/api/customers/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].addressLine").value("123 Main St"));
    }
}
