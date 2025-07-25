/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.catalog.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CatalogOrderItemSummaryResponse(
        Long id,
        Long catalogId,
        String catalogName,
        String catalogType,
        String unitType,
        Long orderId,
        Long orderItemId,
        Integer quantity,
        BigDecimal itemPriceAtOrder,
        LocalDateTime orderDate
) {}
