package com.dropandgo.backend.services;

import com.dropandgo.backend.entity.User;
import com.dropandgo.backend.exceptions.LoginFailedException;
import com.dropandgo.backend.exceptions.RegistrationFailedException;
import com.dropandgo.backend.models.RegisterDetails;

public interface AccountService {
    User loginUser(String email, String password) throws LoginFailedException;

    User registerUser(RegisterDetails details) throws RegistrationFailedException;

    boolean verifyUser(String email, String password);

    String getUserNameByEmail(String email) throws LoginFailedException;
}
