package com.api.oauth.controller;

import com.api.oauth.dto.CreateUserRequest;
import com.api.oauth.dto.LoginRequest;
import com.api.oauth.dto.TokenInfo;
import com.api.oauth.dto.TokenResponse;
import com.api.oauth.services.KeycloakService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final KeycloakService keycloakService;

    public UserController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        keycloakService.createUser(request.username(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> getAuthToken(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(keycloakService.getToken(request.username(), request.password()));
    }

    @PostMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenInfo> validateToken(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(keycloakService.validateToken(authorization));
    }
}
