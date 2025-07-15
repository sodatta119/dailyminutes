/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.geofence.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Read model for Customer summary information relevant to the Geofence module.
 * This entity is populated via events from the Customer module.
 * It links a customer to a specific geofence (e.g., their home geofence).
 */
@Table("DL_GEOFENCE_CUSTOMER_SUMMARY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeofenceCustomerSummaryEntity {

    @Id
    private Long id; // Auto-generated primary key for this summary record

    private Long customerId;

    private Long geofenceId;

    private String customerName;

    private String customerPhoneNumber;
}
