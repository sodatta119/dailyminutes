/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.dto;


import com.dailyminutes.laundry.customer.domain.model.AddressType;

public record CustomerAddressResponse(
        Long id,
        Long customerId,
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
