package com.CloudStore.controllers;

import com.CloudStore.dto.RegisterRequest;
import com.CloudStore.model.User;
import com.CloudStore.service.AuthService;
import com.CloudStore.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class AuthController {
    private final AuthService authService;
    private final S3Service fileService;
    public AuthController(AuthService authService, S3Service fileService) {
        this.authService = authService;
        this.fileService = fileService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){
        User registeredUser = authService.register(registerRequest);
        fileService.createUserInitialFolder(registeredUser.getId());
        return new ResponseEntity<>("Successfully registered, your base package ready for you.", HttpStatus.OK);
    }

}
