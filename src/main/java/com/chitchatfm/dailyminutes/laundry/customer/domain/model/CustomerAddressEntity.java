/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 13/07/25
 */
package com.chitchatfm.dailyminutes.laundry.customer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


/**
 * Represents a specific address for a customer. A customer can have multiple addresses.
 */
@Table("DL_CUSTOMER_ADDRESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerAddressEntity {

    @Id
    private Long id; // Auto-generated primary key for this address record

    private Long customerId; // Foreign key to DL_CUSTOMER

    private AddressType addressType; // e.g., HOME, WORK, SHIPPING, BILLING

    private boolean isDefault; // Flag to mark a default address for the customer

    private String flatApartment;

    private String addressLine;

    private String street; // This will now be nullable

    private String city; // Now nullable

    private String state; // Now nullable

    private String zipCode; // Now nullable

    private String country; // Now nullable

    private String longitude; // Stored as String to handle various precision needs

    private String latitude; // Stored as String to handle various precision needs

    private Long geofenceId; // Foreign key to DL_GEOFENCE
}

