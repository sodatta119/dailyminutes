/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager.dto;

public record ManagerResponse(
        Long id,
        String name,
        String contact
) {}