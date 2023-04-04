package com.dropandgo.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {
    private HttpStatus status;
    private String uploadedFileName;
    private String message;
    private Long timeStamp;
}
