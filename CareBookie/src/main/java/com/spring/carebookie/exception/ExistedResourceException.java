package com.spring.carebookie.exception;

public class ExistedResourceException extends RuntimeException {

    public ExistedResourceException(String message) {
        super(message);
    }
}
