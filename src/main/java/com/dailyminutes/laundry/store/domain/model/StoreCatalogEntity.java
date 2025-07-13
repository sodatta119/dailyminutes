/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 13/07/25
 */
package com.dailyminutes.laundry.store.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a mapping entity linking a Store to a Catalog item.
 */
@Table("DL_STORE_CATALOG") // Renamed table from DL_STORE_CATALOG_SUPPORT
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreCatalogEntity {

    @Id
    private Long id; // Auto-generated primary key for this mapping record

    @Column("STORE_ID")
    private Long storeId; // Foreign key to DL_STORE

    @Column("CATALOG_ID")
    private Long catalogId; // Foreign key to DL_CATALOG
}

