/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 14/07/25
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
 * Read model for Store summary information relevant to the Geofence module.
 * This entity is populated via events from the Store module.
 */
@Table("DL_GEOFENCE_STORE_SUMMARY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeofenceStoreSummaryEntity {

    @Id
    private Long id; // Auto-generated primary key for this summary record

    private Long storeId;

    private Long geofenceId;

    private String storeName;

    private String storeAddress;
}
