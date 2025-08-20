/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.common.exception;

/**
 * The type Entity update exception.
 */
public class EntityUpdateException extends RuntimeException {

    /**
     * Instantiates a new Entity update exception.
     */
    public EntityUpdateException() {
        super();
    }

    /**
     * Instantiates a new Entity update exception.
     *
     * @param message the message
     */
    public EntityUpdateException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Entity update exception.
     *
     * @param ex the ex
     */
    public EntityUpdateException(Exception ex) {
        super(ex);
    }

    /**
     * Instantiates a new Entity update exception.
     *
     * @param message the message
     * @param ex      the ex
     */
    public EntityUpdateException(String message, Exception ex) {
        super(message, ex);
    }
}
