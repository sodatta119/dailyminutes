/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.domain.event;


import com.dailyminutes.laundry.catalog.domain.model.CatalogType;

/**
 * The type Catalog created event.
 */
public record CatalogCreatedEvent(
        Long catalogId,
        CatalogType type,
        String name
) {
    /**
     * Instantiates a new Catalog created event.
     *
     * @param catalogId the catalog id
     * @param type      the type
     * @param name      the name
     */
    public CatalogCreatedEvent {
        if (catalogId == null) throw new IllegalArgumentException("Catalog ID cannot be null");
        if (type == null) throw new IllegalArgumentException("Catalog type cannot be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Catalog name cannot be null or blank");
    }
}
