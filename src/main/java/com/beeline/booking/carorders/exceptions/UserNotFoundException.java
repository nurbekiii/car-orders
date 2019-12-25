package com.beeline.booking.carorders.exceptions;

/**
 * @author NIsaev on 24.12.2019
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String msg){
        super(msg);
    }
}
