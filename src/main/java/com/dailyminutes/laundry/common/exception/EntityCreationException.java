/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.common.exception;

/**
 * The type Entity creation exception.
 */
public class EntityCreationException extends RuntimeException {

    /**
     * Instantiates a new Entity creation exception.
     */
    public EntityCreationException() {
        super();
    }

    /**
     * Instantiates a new Entity creation exception.
     *
     * @param message the message
     */
    public EntityCreationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Entity creation exception.
     *
     * @param ex the ex
     */
    public EntityCreationException(Exception ex) {
        super(ex);
    }

    /**
     * Instantiates a new Entity creation exception.
     *
     * @param message the message
     * @param ex      the ex
     */
    public EntityCreationException(String message, Exception ex) {
        super(message, ex);
    }
}
