/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.common.exception;

/**
 * The type Entity not found exception.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Instantiates a new Entity not found exception.
     */
    public EntityNotFoundException() {
        super();
    }

    /**
     * Instantiates a new Entity not found exception.
     *
     * @param message the message
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Entity not found exception.
     *
     * @param ex the ex
     */
    public EntityNotFoundException(Exception ex) {
        super(ex);
    }

    /**
     * Instantiates a new Entity not found exception.
     *
     * @param message the message
     * @param ex      the ex
     */
    public EntityNotFoundException(String message, Exception ex) {
        super(message, ex);
    }
}
