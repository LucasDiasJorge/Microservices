package com.api.oauth.controller;

import com.api.oauth.services.KeycloakService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final KeycloakService keycloakService;

    public UserController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");

        String response = keycloakService.createUser(username, email, password);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> getAuthToken(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        String accessToken = keycloakService.getToken(username, password);
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KeycloakService.CustomAccessToken> validateToken(@RequestHeader("Authorization") String token) {
        try {
            KeycloakService.CustomAccessToken validatedToken = keycloakService.validateToken(token);
            return ResponseEntity.ok(validatedToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }
}