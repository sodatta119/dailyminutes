package com.chitchatfm.dailyminutes.laundry.manager.domain.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The type Manager entity.
 */
@Table(name = "DL_MANAGER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManagerEntity {

    @Id
    private Long id;

    private String name;

    private String contact; // Could be phone number or email

}

