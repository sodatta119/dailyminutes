package com.chitchatfm.dailyminutes.laundry.geofence.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DL_GEOFENCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeofenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated ID
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(columnDefinition = "TEXT")
    private String polygonCoordinates;

    private String geofenceType;

    private String name;

    private boolean active;

}
