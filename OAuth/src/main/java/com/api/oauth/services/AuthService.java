package com.api.oauth.services;

import com.api.oauth.entities.User;
import com.api.oauth.repositories.UserRepository;
import com.api.oauth.responses.ApiResponse; // Import the new response class
import com.api.oauth.services.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
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

    public ResponseEntity<ApiResponse<Object>> auth(Map<String, Object> body) {
        String email = (String) body.get("email");
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "User not found. Please register first.", null));
        }

        // Use initialized variables
        String scope = "email openid profile roles";
        String grantType = "password";

        try {
            Map<String, Object> response = (Map<String, Object>) httpService.postFormUrlEncoded(url, clientId, clientSecret, scope, grantType, email, (String) body.get("password"));
            String accessToken = (String) response.get("access_token");

            // Successful authentication response
            return ResponseEntity.ok(new ApiResponse<>(true, "Authentication successful.", accessToken));
        } catch (Exception e) {
            log.error("Error during authentication: {}", e.getMessage());
            String errorMessage = interpretError(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, errorMessage, null));
        }
    }

    private String interpretError(String errorMessage) {
        if (errorMessage.contains("invalid_grant")) {
            return "Invalid user credentials. Please check your email and password.";
        } else {
            return "Authentication failed due to an unknown error.";
        }
    }
}
