/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager.dto;

/**
 * The type Manager response.
 */
public record ManagerResponse(
        Long id,
        String name,
        String contact
) {
}