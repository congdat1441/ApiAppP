package com.spring.carebookie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.carebookie.dto.response.ApiErrorResponse;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(ResourceNotFoundException e) {

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {BookDateNotValidException.class})
    public ResponseEntity<ApiErrorResponse> handleBookDateNotValid(BookDateNotValidException e) {

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .code(HttpStatus.NOT_ACCEPTABLE.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ExistedResourceException.class})
    public ResponseEntity<ApiErrorResponse> handleBookDateNotValid(ExistedResourceException e) {

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .code(HttpStatus.NOT_ACCEPTABLE.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAnotherException(Exception e) {

        e.printStackTrace();
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
