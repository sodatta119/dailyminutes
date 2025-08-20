/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.order.domain.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * The type Order item entity.
 */
@Table(name = "DL_ORDER_ITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemEntity {

    @Id
    private Long id;

    private Long orderId;

    private Long catalogId;

    private BigDecimal quantity;

    private BigDecimal itemPriceAtOrder;

    private String notes;

    //private UnitType unitType;
}


