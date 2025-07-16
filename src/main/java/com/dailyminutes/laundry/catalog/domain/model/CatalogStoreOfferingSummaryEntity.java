/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 16/07/25
 */
package com.dailyminutes.laundry.catalog.domain.model; // Updated package name

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Read model for Store Service Offering summary information relevant to the Catalog module.
 * This entity is populated via events from the StoreService and Store modules.
 */
@Table("DL_CATALOG_STORE_OFFERING_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CatalogStoreOfferingSummaryEntity {

    @Id
    private Long id;

    private Long catalogId;

    private String catalogName;

    private String catalogType;

    private String unitType;

    private BigDecimal unitPrice;

    private Long storeId;

    private String storeName;

    private BigDecimal storeSpecificPrice;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private boolean active;
}
