package com.dropandgo.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileNotFoundResponse {
    private HttpStatus status;
    private String message;
    private Long timeStamp;
}
