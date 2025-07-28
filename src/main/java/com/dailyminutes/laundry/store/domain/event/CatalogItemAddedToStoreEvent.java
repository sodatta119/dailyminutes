/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/07/25
 */
package com.dailyminutes.laundry.store.domain.event;

import com.dailyminutes.laundry.store.spi.CallerEvent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CatalogItemAddedToStoreEvent (
        Long storeId,
        Long catalogId,
        BigDecimal storeSpecificPrice,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        boolean active
) implements CallerEvent {}