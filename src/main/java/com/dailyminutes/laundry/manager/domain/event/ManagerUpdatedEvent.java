/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 26/07/25
 */
package com.dailyminutes.laundry.manager.domain.event;

public record ManagerUpdatedEvent(
        Long managerId,
        String newName,
        String newContact
) {}