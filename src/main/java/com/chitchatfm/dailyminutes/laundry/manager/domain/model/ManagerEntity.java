package com.chitchatfm.dailyminutes.laundry.manager.domain.model;


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
@Table(name = "DL_MANAGER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManagerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contact; // Could be phone number or email

    // No direct JPA relationship (e.g., @OneToMany List<StoreEntity>) here.
    // The Manager module does not directly know about the Store entities it manages.
}

