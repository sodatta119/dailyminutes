/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.chitchatfm.dailyminutes.laundry.order.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

/**
 * The type Order entity.
 */
@Table(name = "DL_ORDER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderEntity {

    @Id
    private Long id; // orderId

    private Long storeId;

    private Long customerId;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private BigDecimal totalAmount;
}
