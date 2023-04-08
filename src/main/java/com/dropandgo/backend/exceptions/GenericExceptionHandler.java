package com.dropandgo.backend.exceptions;

import com.dropandgo.backend.constants.AccountConstant;
import com.dropandgo.backend.responses.GenericExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericExceptionResponse> handleGenericExceptions(Exception e, WebRequest req) {
        return ResponseEntity.badRequest().body(
                new GenericExceptionResponse(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        AccountConstant.getTime()
                )
        );
    }
}
