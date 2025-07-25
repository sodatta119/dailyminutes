/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.domain.event;


public record CatalogDeletedEvent(Long catalogId) {
    public CatalogDeletedEvent {
        if (catalogId == null) throw new IllegalArgumentException("Catalog ID cannot be null");
    }
}