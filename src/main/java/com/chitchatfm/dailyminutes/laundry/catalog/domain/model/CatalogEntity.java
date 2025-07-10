package com.chitchatfm.dailyminutes.laundry.catalog.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "DL_CATALOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = false)
    private CatalogType type;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = false)
    private UnitType unit;

    @Column(nullable = false, precision = 10, scale = 2) // Precision for currency
    private BigDecimal unitPrice;
}

