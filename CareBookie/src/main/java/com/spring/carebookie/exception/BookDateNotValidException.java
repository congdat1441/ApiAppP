package com.spring.carebookie.exception;

public class BookDateNotValidException extends RuntimeException{
    public BookDateNotValidException(String message) {
        super(message);
    }
}
