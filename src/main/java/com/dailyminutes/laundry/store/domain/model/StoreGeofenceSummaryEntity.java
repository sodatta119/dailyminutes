/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.store.domain.model; // Updated package name

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Read model for Geofence summary information relevant to the Store module.
 * This entity is populated via events from the Geofence module or explicit mappings.
 */
@Table("DL_STORE_GEOFENCE_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreGeofenceSummaryEntity {

    @Id
    private Long id;

    private Long storeId;

    private Long geofenceId;

    private String geofenceExternalId;

    private String geofenceName;

    private String geofenceType;

    private boolean active;
}
