/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.domain.event;


import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;

import java.math.BigDecimal;

public record CatalogUpdatedEvent(
        Long catalogId,
        CatalogType type,
        String name,
        UnitType unit,
        BigDecimal unitPrice
) {
    public CatalogUpdatedEvent {
        if (catalogId == null) throw new IllegalArgumentException("Catalog ID cannot be null");
        if (type == null) throw new IllegalArgumentException("Catalog type cannot be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Catalog name cannot be null or blank");
        if (unit == null) throw new IllegalArgumentException("Unit type cannot be null");
        if (unitPrice == null) throw new IllegalArgumentException("Unit price cannot be null");
    }
}
