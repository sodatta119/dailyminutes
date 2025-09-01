/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 29/08/25
 */
package com.dailyminutes.laundry.integration.manychat.dto;

/**
 * The type Custom field.
 */
public record CustomField(
        long id,
        String name,
        String type,        // "number","text","date","boolean"
        String description,
        Object value        // keep generic, we'll coerce
) {}

