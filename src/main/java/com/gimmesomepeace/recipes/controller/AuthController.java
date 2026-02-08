package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.dto.request.LoginRequest;
import com.gimmesomepeace.recipes.dto.request.RegistrationRequest;
import com.gimmesomepeace.recipes.dto.response.LoginResponse;
import com.gimmesomepeace.recipes.dto.response.UserResponse;
import com.gimmesomepeace.recipes.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    ResponseEntity<UserResponse> registration(@RequestBody RegistrationRequest request) {
        UserResponse user = service.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201
                .body(user);
    }

    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }
}
