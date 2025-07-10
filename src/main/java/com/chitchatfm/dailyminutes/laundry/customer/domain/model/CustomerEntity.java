package com.chitchatfm.dailyminutes.laundry.customer.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DL_CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerEntity {

    @Id
    @Column(length = 20, nullable = false, unique = true) // Phone number as PK, assuming max 20 chars
    private String id;

    @Column(nullable = false)
    private String subscriberId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true) // Email should typically be unique
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private Long homeGeofenceId;

}

