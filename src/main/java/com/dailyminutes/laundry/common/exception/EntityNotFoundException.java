/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.common.exception;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(){
        super();
    }

    public EntityNotFoundException(String message){
        super(message);
    }

    public EntityNotFoundException(Exception ex){
        super(ex);
    }

    public EntityNotFoundException(String message, Exception ex){
        super(message, ex);
    }
}
