package com.dropandgo.backend.controllers;

import com.dropandgo.backend.constants.AccountConstant;
import com.dropandgo.backend.entity.User;
import com.dropandgo.backend.exceptions.LoginFailedException;
import com.dropandgo.backend.exceptions.RegistrationFailedException;
import com.dropandgo.backend.exceptions.UnauthorizedAccessException;
import com.dropandgo.backend.models.ChangePasswordModel;
import com.dropandgo.backend.models.ForgetPasswordModel;
import com.dropandgo.backend.models.LoginDetails;
import com.dropandgo.backend.models.RegisterDetails;
import com.dropandgo.backend.responses.LoginResponse;
import com.dropandgo.backend.responses.PasswordUpdateResponse;
import com.dropandgo.backend.responses.RegisterResponse;
import com.dropandgo.backend.services.AccountService;
import com.dropandgo.backend.services.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AdminService adminService;

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

    @PostMapping("/account/forgetPassword")
    public PasswordUpdateResponse forgetPasswordRequest(
            @RequestHeader("adminId") String adminId,
            @RequestHeader("adminPassword") String adminPassword,
            @RequestBody ForgetPasswordModel forgetPasswordModel
    ) throws UnauthorizedAccessException, LoginFailedException {
        if (forgetPasswordModel.getEmail() != null && forgetPasswordModel.getChangedPassword() != null) {
            if (adminService.isAdmin(adminId, adminPassword)) {
                User user = accountService.updatePassword(forgetPasswordModel.getEmail(), forgetPasswordModel.getChangedPassword());
                return new PasswordUpdateResponse(
                        HttpStatus.OK,
                        user,
                        "Password changed for '" + forgetPasswordModel.getEmail() + "' to " + user.getPassword(),
                        AccountConstant.getTime()
                );
            }
            throw new UnauthorizedAccessException("Unauthorized to update database");
        }
        throw new LoginFailedException("Incomplete details to update password");
    }

    @PostMapping("/account/changePassword")
    public PasswordUpdateResponse changePasswordRequest(
            @RequestBody ChangePasswordModel changePasswordModel
    ) throws UnauthorizedAccessException, LoginFailedException {
        if (changePasswordModel.getEmail() != null && changePasswordModel.getCurrentPassword() != null && changePasswordModel.getChangedPassword() != null) {
            User user = accountService.changePassword(changePasswordModel.getEmail(), changePasswordModel.getCurrentPassword(), changePasswordModel.getChangedPassword());
            return new PasswordUpdateResponse(
                    HttpStatus.OK,
                    user,
                    "Password changed for '" + changePasswordModel.getEmail() + "' to " + user.getPassword(),
                    AccountConstant.getTime()
            );
        }
        throw new LoginFailedException("Incomplete details to update password");
    }
}
