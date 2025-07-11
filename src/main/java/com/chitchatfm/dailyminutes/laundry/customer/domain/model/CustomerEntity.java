package com.chitchatfm.dailyminutes.laundry.customer.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The type Customer entity.
 */
@Table(name = "DL_CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerEntity {

    @Id
    private String id;

    private String subscriberId;

    private String name;

    private String email;

    private String address;

    private Long homeGeofenceId;

}

