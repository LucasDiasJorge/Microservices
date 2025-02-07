package com.api.keycloak.controller;

import com.api.keycloak.services.KeycloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final KeycloakService keycloakService;

    public UserController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");

        String response = keycloakService.createUser(username, email, password);
        return ResponseEntity.ok(response);
    }
}
