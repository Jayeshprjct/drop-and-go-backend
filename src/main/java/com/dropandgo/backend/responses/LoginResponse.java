package com.dropandgo.backend.responses;

import com.dropandgo.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private HttpStatus status;
    private User requestedUser;
    private String message;
    private Long timeStamp;
}
