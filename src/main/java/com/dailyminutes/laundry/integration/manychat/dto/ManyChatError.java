/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/08/25
 */
package com.dailyminutes.laundry.integration.manychat.dto;

/**
 * The type Many chat error.
 */
public record ManyChatError(
        String code,
        String message
) {}
