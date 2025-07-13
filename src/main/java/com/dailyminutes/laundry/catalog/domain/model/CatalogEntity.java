/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */

package com.dailyminutes.laundry.catalog.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * The type Catalog entity.
 */
@Table(name = "DL_CATALOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CatalogEntity {

    @Id
    private Long id;

    private CatalogType type;

    private String name;

    private UnitType unit;

    private BigDecimal unitPrice;
}

