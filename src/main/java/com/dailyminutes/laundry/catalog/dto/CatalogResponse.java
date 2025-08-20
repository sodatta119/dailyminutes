/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.dto;

import com.dailyminutes.laundry.catalog.domain.model.CatalogType;

/**
 * The type Catalog response.
 */
public record CatalogResponse(
        Long id,
        CatalogType type,
        String name
) {
}