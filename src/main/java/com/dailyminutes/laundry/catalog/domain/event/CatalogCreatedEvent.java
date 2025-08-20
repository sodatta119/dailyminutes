/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.domain.event;


import com.dailyminutes.laundry.catalog.domain.model.CatalogType;

public record CatalogCreatedEvent(
        Long catalogId,
        CatalogType type,
        String name
) {
    public CatalogCreatedEvent {
        if (catalogId == null) throw new IllegalArgumentException("Catalog ID cannot be null");
        if (type == null) throw new IllegalArgumentException("Catalog type cannot be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Catalog name cannot be null or blank");
    }
}
