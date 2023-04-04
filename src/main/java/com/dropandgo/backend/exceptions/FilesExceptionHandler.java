package com.dropandgo.backend.exceptions;

import com.dropandgo.backend.constants.AccountConstant;
import com.dropandgo.backend.responses.FileNotFoundResponse;
import com.dropandgo.backend.responses.UnauthorizedAccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.FileNotFoundException;

@ControllerAdvice
public class FilesExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<FileNotFoundResponse> fileNotFoundExceptionHandler(FileNotFoundException e, WebRequest req) {
        return ResponseEntity.badRequest()
                .body(
                        new FileNotFoundResponse(
                                HttpStatus.BAD_REQUEST,
                                e.getMessage(),
                                AccountConstant.getTime()
                        )
                );
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<UnauthorizedAccessResponse> unauthorizedAccessExceptionHandler(UnauthorizedAccessException e, WebRequest req) {
        return ResponseEntity.status(
                HttpStatus.UNAUTHORIZED
        ).body(
                new UnauthorizedAccessResponse(
                        HttpStatus.UNAUTHORIZED,
                        e.getMessage(),
                        AccountConstant.getTime()
                )
        );
    }
}
