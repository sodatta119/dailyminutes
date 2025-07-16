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
import java.time.LocalDateTime;


/**
 * Read model for Order Item summary information relevant to the Catalog module.
 * This entity is populated via events from the Order module (Order and OrderItem entities).
 */
@Table("DL_CATALOG_ORDER_ITEM_SUMMARY") // Table for the read model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CatalogOrderItemSummaryEntity {

    @Id
    private Long id;

    private Long catalogId;

    private String catalogName;

    private String catalogType;

    private String unitType;

    private Long orderId;

    private Long orderItemId;

    private Integer quantity;

    private BigDecimal itemPriceAtOrder;

    private LocalDateTime orderDate;
}
