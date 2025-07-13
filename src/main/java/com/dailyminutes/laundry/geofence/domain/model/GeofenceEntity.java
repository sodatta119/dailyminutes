/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.geofence.domain.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The type Geofence entity.
 */
@Table(name = "DL_GEOFENCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeofenceEntity {

    @Id
    private Long id;

    private String polygonCoordinates;

    private String geofenceType;

    private String name;

    private boolean active;

}
