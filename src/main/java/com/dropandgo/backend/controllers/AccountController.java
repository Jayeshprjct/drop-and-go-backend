package com.dropandgo.backend.controllers;

import com.dropandgo.backend.constants.AccountConstant;
import com.dropandgo.backend.entity.User;
import com.dropandgo.backend.exceptions.LoginFailedException;
import com.dropandgo.backend.exceptions.RegistrationFailedException;
import com.dropandgo.backend.models.LoginDetails;
import com.dropandgo.backend.models.RegisterDetails;
import com.dropandgo.backend.responses.LoginResponse;
import com.dropandgo.backend.responses.RegisterResponse;
import com.dropandgo.backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public LoginResponse loginPostRequest(@RequestBody LoginDetails details) throws LoginFailedException {
        User returnedUser = accountService.loginUser(details.getEmail(), details.getPassword());
        return new LoginResponse(
                HttpStatus.OK,
                returnedUser,
                "User is valid.",
                AccountConstant.getTime()
        );
    }

    @PostMapping("/register")
    public RegisterResponse registerPostRequest(@RequestBody RegisterDetails details) throws RegistrationFailedException {
        User returnedUser = accountService.registerUser(details);
        return new RegisterResponse(
                HttpStatus.OK,
                returnedUser,
                details.getName() + " registered successfully!",
                AccountConstant.getTime()
        );
    }
}
