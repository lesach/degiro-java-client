/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.lesach.exceptions;

/**
 *
 * @author indiketa
 */
public class DUnauthorizedException extends DeGiroException {

    public DUnauthorizedException() {
    }

    public DUnauthorizedException(String message) {
        super(message);
    }

    public DUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DUnauthorizedException(Throwable cause) {
        super(cause);
    }
    
    

}
