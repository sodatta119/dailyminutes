/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.payment.domain.model;

/**
 * The enum Payment status.
 */
public enum PaymentStatus {
    /**
     * Pending payment status.
     */
    PENDING,
    /**
     * Completed payment status.
     */
    COMPLETED,
    /**
     * Failed payment status.
     */
    FAILED,
    /**
     * Refunded payment status.
     */
    REFUNDED,
    /**
     * Cancelled payment status.
     */
    CANCELLED
}
