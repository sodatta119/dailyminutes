/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.dto;


import com.dailyminutes.laundry.customer.domain.model.AddressType;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerAddressRequest(
        @NotNull(message = "Customer ID cannot be null")
        Long customerId,
        @NotNull(message = "Address type cannot be null")
        AddressType addressType,
        boolean isDefault,
        String flatApartment,
        String addressLine,
        String street,
        String city,
        String state,
        String zipCode,
        String country,
        String longitude,
        String latitude,
        Long geofenceId
) {}
