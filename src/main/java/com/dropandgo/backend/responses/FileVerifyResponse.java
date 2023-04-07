package com.dropandgo.backend.responses;

import com.dropandgo.backend.entity.DropAndGoFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileVerifyResponse {
    private HttpStatus status;
    private DropAndGoFile verifiedFile;
    private String message;
    private Long timeStamp;
}
