/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 15/07/25
 */
package com.dailyminutes.laundry.payment.domain.model;

/**
 * The enum Payment method.
 */
public enum PaymentMethod {
    /**
     * Credit card payment method.
     */
    CREDIT_CARD,
    /**
     * Debit card payment method.
     */
    DEBIT_CARD,
    /**
     * Net banking payment method.
     */
    NET_BANKING,
    /**
     * Upi payment method.
     */
    UPI,
    /**
     * Cash payment method.
     */
    CASH,
    /**
     * Wallet payment method.
     */
    WALLET,
    /**
     * Other payment method.
     */
    OTHER
}
