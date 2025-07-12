/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.order.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The type Order item metadata entity.
 */
@Table(name = "DL_ORDER_ITEM_METADATA") // New table name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemMetadataEntity {

    @Id
    private Long id;

    private Long orderItemId;

    private String type; // e.g., "IMAGE", "TEXT_NOTE", "SPECIAL_INSTRUCTION"

    private String itemValue; // Content of the metadata (e.g., image URL, text)

    private String description; // Optional description for the metadata
}

