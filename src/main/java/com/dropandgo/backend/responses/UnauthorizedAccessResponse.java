package com.dropandgo.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnauthorizedAccessResponse {
    private HttpStatus status;
    private String message;
    private Long timeStamp;
}
