package com.CloudStore.service;

import com.CloudStore.dto.RegisterRequest;
import com.CloudStore.model.User;

public interface AuthService {
    User register(RegisterRequest registerRequest);
}
