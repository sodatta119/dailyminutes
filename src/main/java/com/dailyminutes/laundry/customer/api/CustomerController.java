/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.api;

import com.dailyminutes.laundry.customer.dto.*;
import com.dailyminutes.laundry.customer.service.CustomerQueryService;
import com.dailyminutes.laundry.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return new ResponseEntity<>(customerService.createCustomer(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(customerService.updateCustomer(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return customerQueryService.findCustomerById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerQueryService.findAllCustomers());
    }

    @PostMapping("/addresses")
    public ResponseEntity<CustomerAddressResponse> addAddress(@Valid @RequestBody CreateCustomerAddressRequest request) {
        return new ResponseEntity<>(customerService.addAddress(request), HttpStatus.CREATED);
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(@PathVariable Long id, @Valid @RequestBody UpdateCustomerAddressRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(customerService.updateAddress(request));
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> removeAddress(@PathVariable Long id) {
        customerService.removeAddress(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<CustomerAddressResponse>> getAddressesByCustomerId(@PathVariable Long id) {
        return ResponseEntity.ok(customerQueryService.findAddressesByCustomerId(id));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<CustomerOrderSummaryResponse>> getOrderSummariesByCustomerId(@PathVariable Long id) {
        return ResponseEntity.ok(customerQueryService.findOrderSummariesByCustomerId(id));
    }
}
