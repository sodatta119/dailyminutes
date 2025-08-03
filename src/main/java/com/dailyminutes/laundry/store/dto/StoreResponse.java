/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.store.dto;

public record StoreResponse(
        Long id,
        String name,
        String address,
        String contactNumber,
        String email,
        Long managerId
) {
}