package com.api.keycloak.services;

import jakarta.ws.rs.core.Response;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakService {

    private final Keycloak keycloak;
    private final String clientId;
    private final String clientSecret;
    private final String serverUrl;
    private final String realm;

    public KeycloakService(@Value("${keycloak.server-url}") String serverUrl,
                           @Value("${keycloak.client-id}") String clientId,
                           @Value("${keycloak.client-secret}") String clientSecret,
                           @Value("${keycloak.realm}") String realm) {
        this.serverUrl = serverUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.realm = realm;

        this.keycloak = createKeycloakClient();
    }

    private Keycloak createKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }

    public String createUser(String username, String email, String password) {
        UserRepresentation user = createUserRepresentation(username, email, password);

        try (Response response = keycloak.realm(realm).users().create(user)) {
            if (response.getStatus() == 201) {
                return "Usuário criado no Keycloak com sucesso!";
            } else {
                String errorMessage = response.readEntity(String.class);
                return "Erro ao criar usuário: " + response.getStatus() + " - " + errorMessage;
            }
        } catch (Exception e) {
            return "Erro ao criar usuário: " + e.getMessage();
        }
    }

    private UserRepresentation createUserRepresentation(String username, String email, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRealmRoles(Collections.singletonList("user"));

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        return user;
    }

    public String getToken(String username, String password) {
        Keycloak keycloakUser = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .grantType("password")
                .build();

        return keycloakUser.tokenManager().getAccessToken().getToken();
    }

    public CustomAccessToken validateToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            TokenVerifier<AccessToken> verifier = TokenVerifier.create(token, AccessToken.class)
                    .withChecks(
                            TokenVerifier.SUBJECT_EXISTS_CHECK, // Verifica se o subject existe
                            TokenVerifier.IS_ACTIVE // Verifica se o token está ativo (não expirado)
                    );

            AccessToken accessToken = verifier.getToken();

            if (!accessToken.getIssuer().equals(serverUrl + "/realms/" + realm)) {
                throw new VerificationException("Token issuer is invalid");
            }

            return new CustomAccessToken(
                    accessToken.getId(),
                    accessToken.getSubject(),
                    accessToken.getIssuer()
            );
        } catch (VerificationException e) {
            throw new RuntimeException("Token validation failed: " + e.getMessage(), e);
        }
    }

    public static class CustomAccessToken {
        private String id;
        private String subject;
        private String issuer;

        // Construtor padrão (necessário para o Jackson)
        public CustomAccessToken() {}

        // Construtor com parâmetros
        public CustomAccessToken(String id, String subject, String issuer) {
            this.id = id;
            this.subject = subject;
            this.issuer = issuer;
        }

        // Getters e Setters (necessários para o Jackson)
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        @Override
        public String toString() {
            return "CustomAccessToken{" +
                    "id='" + id + '\'' +
                    ", subject='" + subject + '\'' +
                    ", issuer='" + issuer + '\'' +
                    '}';
        }
    }
}