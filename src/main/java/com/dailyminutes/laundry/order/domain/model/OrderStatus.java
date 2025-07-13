/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes.laundry.order.domain.model;

/**
 * The enum Order status.
 */
// Enum for the status of an order
public enum OrderStatus {
    /**
     * Pending order status.
     */
    PENDING,
    /**
     * Accepted order status.
     */
    ACCEPTED,
    /**
     * In process order status.
     */
    IN_PROCESS,
    /**
     * Ready for pickup order status.
     */
    READY_FOR_PICKUP,
    /**
     * Delivered order status.
     */
    DELIVERED,
    /**
     * Cancelled order status.
     */
    CANCELLED
}
