/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.common.exception;

public class EntityPersistenceException extends IllegalArgumentException {
    public EntityPersistenceException() {
        super();
    }

    public EntityPersistenceException(Exception e) {
        super(e);
    }

    public EntityPersistenceException(String message) {
        super(message);
    }

    public EntityPersistenceException(String message, Exception e) {
        super(message, e);
    }
}
