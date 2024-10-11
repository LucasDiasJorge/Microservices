package com.api.oauth.services;

import com.api.oauth.entities.User;
import com.api.oauth.repositories.UserRepository;
import com.api.oauth.services.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private HttpService httpService;

    @Value("${keycloak.auth.openid-token}")
    private String url;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.auth.client-secret}")
    private String clientSecret;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, HttpService httpService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.httpService = httpService;
    }

    public ResponseEntity<Map<String, Object>> auth(Map<String, Object> body) {
        String email = (String) body.get("email");
        String pass = bCryptPasswordEncoder.encode((String) body.get("password"));

        Optional<User> user = userRepository.findByEmail(email);

        // Use initialized variables
        String scope = "email openid profile roles";
        String grantType = "password";
        String username = "lucas_jorg@hotmail.com";
        String password = "123456";

        System.out.println(httpService.postFormUrlEncoded(url, clientId, clientSecret, scope, grantType, username, password).getBody().toString());

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!(user.get().getPassword().equals(pass))) {
            return ResponseEntity.badRequest().build();
        }

        return null;
    }
}
