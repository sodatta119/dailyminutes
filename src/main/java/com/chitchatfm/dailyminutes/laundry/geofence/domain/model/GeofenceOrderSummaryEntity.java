/**
 * @author Somendra Datta <sodatta@example.com>
 * @version 14/07/25
 */
package com.chitchatfm.dailyminutes.laundry.geofence.domain.model; // Placing it in the geofence module's domain model

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Read model for Order summary information relevant to the Geofence module.
 * This entity is populated via events from the Order module.
 * It links an order to a specific geofence (e.g., the customer's home geofence, or the store's geofence).
 */
@Table("DL_GEOFENCE_ORDER_SUMMARY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeofenceOrderSummaryEntity {

    @Id
    private Long id; // Auto-generated primary key for this summary record

    private Long orderId; // The Order ID from the Order module (unique for this summary)

    private Long geofenceId; // The ID of the geofence associated with this order summary

    private LocalDateTime orderDate;

    private String status;

    private BigDecimal totalAmount;

    private Long customerId;

    private Long storeId;
}

