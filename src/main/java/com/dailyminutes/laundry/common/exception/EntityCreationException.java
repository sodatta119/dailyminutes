/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 18/07/25
 */
package com.dailyminutes.laundry.common.exception;

public class EntityCreationException extends RuntimeException{

    public EntityCreationException(){
        super();
    }

    public EntityCreationException(String message){
        super(message);
    }

    public EntityCreationException(Exception ex){
        super(ex);
    }

    public EntityCreationException(String message, Exception ex){
        super(message, ex);
    }
}
