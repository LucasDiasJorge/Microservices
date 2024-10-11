package com.api.oauth.controllers;

import com.api.oauth.responses.ApiResponse;
import com.api.oauth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<Object>> auth(@RequestBody Map<String, Object> body) {
        return authService.auth(body);
    }
}
