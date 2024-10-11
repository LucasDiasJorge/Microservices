package com.api.oauth.services;

import com.api.oauth.entities.User;
import com.api.oauth.repositories.UserRepository;
import com.api.oauth.services.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private HttpService httpService;

    @Value("${keycloak.auth.openid-token}")
    private String url;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.auth.client-secret}")
    private String clientSecret;

    @Autowired
    public AuthService(UserRepository userRepository, HttpService httpService) {
        this.userRepository = userRepository;
        this.httpService = httpService;
    }

    public Object auth(Map<String, Object> body) {
        String email = (String) body.get("email");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        // Use initialized variables
        String scope = "email openid profile roles";
        String grantType = "password";

        try {
            return httpService.postFormUrlEncoded(url, clientId, clientSecret, scope, grantType, email, (String) body.get("password"));
        } catch (Exception e) {
        log.error("Error during authentication: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(interpretError(e.getMessage()));
        }
    }

    private String interpretError(String errorMessage) {
        if (errorMessage.contains("invalid_grant")) {
            return "Invalid user credentials. Please check your email and password.";
        } else if (errorMessage.contains("user_not_found")) {
            return "User not found. Please register first.";
        } else {
            return "Authentication failed due to an unknown error.";
        }
    }
}
