/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.dto;

import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The type Update catalog request.
 */
public record UpdateCatalogRequest(
        @NotNull(message = "Catalog ID cannot be null for update")
        Long id,
        @NotNull(message = "Catalog type cannot be null")
        CatalogType type,
        @NotBlank(message = "Catalog name cannot be blank")
        String name,
        @NotNull(message = "Unit type cannot be null")
        UnitType unit
) {
}
