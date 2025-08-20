/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.common.exception;

/**
 * The type Entity persistence exception.
 */
public class EntityPersistenceException extends IllegalArgumentException {
    /**
     * Instantiates a new Entity persistence exception.
     */
    public EntityPersistenceException() {
        super();
    }

    /**
     * Instantiates a new Entity persistence exception.
     *
     * @param e the e
     */
    public EntityPersistenceException(Exception e) {
        super(e);
    }

    /**
     * Instantiates a new Entity persistence exception.
     *
     * @param message the message
     */
    public EntityPersistenceException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Entity persistence exception.
     *
     * @param message the message
     * @param e       the e
     */
    public EntityPersistenceException(String message, Exception e) {
        super(message, e);
    }
}
