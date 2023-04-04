package com.dropandgo.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDeleteResponse {
    private HttpStatus status;
    private String fileName;
    private String message;
    private Boolean deleted;
    private Long timestamp;
}
