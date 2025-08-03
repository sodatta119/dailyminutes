/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.dto;

import com.dailyminutes.laundry.catalog.domain.model.CatalogType;
import com.dailyminutes.laundry.catalog.domain.model.UnitType;

import java.math.BigDecimal;

public record CatalogResponse(
        Long id,
        CatalogType type,
        String name,
        UnitType unit,
        BigDecimal unitPrice
) {
}