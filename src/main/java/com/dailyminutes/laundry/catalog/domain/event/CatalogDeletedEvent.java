/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.domain.event;


/**
 * The type Catalog deleted event.
 */
public record CatalogDeletedEvent(Long catalogId) {
    /**
     * Instantiates a new Catalog deleted event.
     *
     * @param catalogId the catalog id
     */
    public CatalogDeletedEvent {
        if (catalogId == null) throw new IllegalArgumentException("Catalog ID cannot be null");
    }
}