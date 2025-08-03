/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.dto;

import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateCatalogRequest(
        @NotNull(message = "Catalog type cannot be null")
        CatalogType type,
        @NotBlank(message = "Catalog name cannot be blank")
        String name,
        @NotNull(message = "Unit type cannot be null")
        UnitType unitType,
        @NotNull(message = "Unit price cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be positive")
        BigDecimal unitPrice
) {
}

