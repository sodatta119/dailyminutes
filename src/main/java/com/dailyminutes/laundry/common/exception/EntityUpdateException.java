/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.common.exception;

public class EntityUpdateException extends RuntimeException {

    public EntityUpdateException() {
        super();
    }

    public EntityUpdateException(String message) {
        super(message);
    }

    public EntityUpdateException(Exception ex) {
        super(ex);
    }

    public EntityUpdateException(String message, Exception ex) {
        super(message, ex);
    }
}
