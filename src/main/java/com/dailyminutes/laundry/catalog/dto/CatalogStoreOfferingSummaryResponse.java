/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The type Catalog store offering summary response.
 */
public record CatalogStoreOfferingSummaryResponse(
        Long id,
        Long catalogId,
        String catalogName,
        String catalogType,
        String unitType,
        BigDecimal unitPrice,
        Long storeId,
        String storeName,
        BigDecimal storeSpecificPrice,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        boolean active
) {
}
