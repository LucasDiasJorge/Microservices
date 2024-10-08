package com.api.oauth.controllers;

import com.api.oauth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    public AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<Map<String,Object>> auth(@RequestBody Map<String, Object> body){

        System.out.println("CUDIMEL");

        return ResponseEntity.ok(authService.auth(body).getBody());

    }

}
