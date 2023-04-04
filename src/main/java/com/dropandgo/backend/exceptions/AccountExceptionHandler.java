package com.dropandgo.backend.exceptions;

import com.dropandgo.backend.constants.AccountConstant;
import com.dropandgo.backend.responses.LoginResponse;
import com.dropandgo.backend.responses.RegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<LoginResponse> loginFailedExceptionHandler(LoginFailedException e, WebRequest req) {
        return ResponseEntity.status(
                HttpStatus.BAD_REQUEST
        ).body(
                new LoginResponse(
                        HttpStatus.BAD_REQUEST,
                        null,
                        e.getMessage(),
                        AccountConstant.getTime()
                )
        );
    }

    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<RegisterResponse> registrationFailedExceptionHandler(RegistrationFailedException e, WebRequest req) {
        return ResponseEntity.status(
                HttpStatus.BAD_REQUEST
        ).body(
                new RegisterResponse(
                        HttpStatus.BAD_REQUEST,
                        null,
                        e.getMessage(),
                        AccountConstant.getTime()
                )
        );
    }
}
