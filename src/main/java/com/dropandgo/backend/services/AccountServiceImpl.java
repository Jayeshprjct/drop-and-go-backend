package com.dropandgo.backend.services;

import com.dropandgo.backend.entity.User;
import com.dropandgo.backend.exceptions.LoginFailedException;
import com.dropandgo.backend.exceptions.RegistrationFailedException;
import com.dropandgo.backend.exceptions.UnauthorizedAccessException;
import com.dropandgo.backend.models.RegisterDetails;
import com.dropandgo.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public User loginUser(String email, String password) throws LoginFailedException {
        Optional<User> mayBeUser = accountRepository.findByEmail(email);
        if (mayBeUser.isPresent()) {
            User user = mayBeUser.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
            throw new LoginFailedException("Incorrect Password");
        }
        throw new LoginFailedException("Invalid User");
    }

    @Override
    public User registerUser(RegisterDetails details) throws RegistrationFailedException {
        if (details.getName() != null && details.getEmail() != null && details.getPassword() != null) {
            if (accountRepository.findByEmail(details.getEmail()).isPresent()) {
                throw new RegistrationFailedException("User already exists!");
            }
            User user = new User(
                    details.getName(),
                    0L,
                    0L,
                    details.getEmail(),
                    details.getPassword()
            );
            return accountRepository.save(user);
        }
        throw new RegistrationFailedException("Incomplete details");
    }

    @Override
    public boolean verifyUser(String email, String password) {
        Optional<User> mayBeUser = accountRepository.findByEmail(email);
        if (mayBeUser.isPresent()) {
            User user = mayBeUser.get();
            if (user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUserNameByEmail(String email) throws LoginFailedException {
        Optional<User> mayBeUser = accountRepository.findByEmail(email);
        if (mayBeUser.isPresent()) {
            User user = mayBeUser.get();
            return user.getName();
        }
        throw new LoginFailedException("User not found!");
    }

    @Override
    public User updatePassword(String email, String changedPassword) throws LoginFailedException {
        Optional<User> mayBeUser = accountRepository.findByEmail(email);
        if (mayBeUser.isPresent()) {
            User user = mayBeUser.get();
            user.setPassword(changedPassword);
            return accountRepository.save(user);
        }
        throw new LoginFailedException("User not found!");
    }

    @Override
    public User changePassword(String email, String currentPassword, String changedPassword) throws UnauthorizedAccessException {
        if (verifyUser(email, currentPassword)) {
            Optional<User> maybeUser = accountRepository.findByEmail(email);
            if (maybeUser.isPresent()) {
                User user = maybeUser.get();
                user.setPassword(changedPassword);
                return accountRepository.save(user);
            }
        }
        throw new UnauthorizedAccessException("Unauthorized to change password");
    }
}
