package com.CloudStore.service.impl;

import com.CloudStore.dto.RegisterRequest;
import com.CloudStore.model.User;
import com.CloudStore.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserServiceImpl userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        User newUser = new User(registerRequest.getUsername(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()));
        return userService.create(newUser);
    }
}
