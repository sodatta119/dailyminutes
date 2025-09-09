/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.order.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private String externalId; // orderId

    private Long storeId;

    private Long customerId;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private BigDecimal totalAmount;
//
//    private String orderAddress;
//
//    private Long orderGeofenceId;
}
