/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.customer.domain.event;

import com.dailyminutes.laundry.customer.domain.model.AddressType;

/**
 * The type Customer address added event.
 */
public record CustomerAddressAddedEvent(
        Long addressId,
        Long customerId,
        String customerName,
        String customerPhoneNumber,
        AddressType addressType,
        boolean isDefault,
        String addressLine,
        String city,
        String zipCode,
        Long geofenceId
) {
}
