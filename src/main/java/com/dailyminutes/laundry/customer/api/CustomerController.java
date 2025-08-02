/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.api;

import com.dailyminutes.laundry.customer.dto.*;
import com.dailyminutes.laundry.customer.service.CustomerQueryService;
import com.dailyminutes.laundry.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers and their addresses")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerQueryService customerQueryService;

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return new ResponseEntity<>(customerService.createCustomer(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(customerService.updateCustomer(request));
    }

    @Operation(summary = "Delete a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return customerQueryService.findCustomerById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @Operation(summary = "Get all customers")
    @ApiResponse(responseCode = "200", description = "List of all customers")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerQueryService.findAllCustomers());
    }

    @Operation(summary = "Add an address for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping("/addresses")
    public ResponseEntity<CustomerAddressResponse> addAddress(@Valid @RequestBody CreateCustomerAddressRequest request) {
        return new ResponseEntity<>(customerService.addAddress(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a customer's address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PutMapping("/addresses/{id}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(@PathVariable Long id, @Valid @RequestBody UpdateCustomerAddressRequest request) {
        if (!id.equals(request.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in request body.");
        }
        return ResponseEntity.ok(customerService.updateAddress(request));
    }

    @Operation(summary = "Remove an address from a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address removed successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> removeAddress(@PathVariable Long id) {
        customerService.removeAddress(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all addresses for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found addresses for the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<CustomerAddressResponse>> getAddressesByCustomerId(@PathVariable Long id) {
        return ResponseEntity.ok(customerQueryService.findAddressesByCustomerId(id));
    }
}
