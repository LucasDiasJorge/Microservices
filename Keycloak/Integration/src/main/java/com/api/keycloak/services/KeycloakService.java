package com.api.keycloak.services;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakService(@Value("${keycloak.server-url}") String serverUrl,
                           @Value("${keycloak.client-id}") String clientId,
                           @Value("${keycloak.client-secret}") String clientSecret,
                           @Value("${keycloak.realm}") String realm) {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }

    public String createUser(String username, String email, String password) {
        UserRepresentation user = getUserRepresentation(username, email, password);

        try (Response response = keycloak.realm(realm).users().create(user)) {

            if (response.getStatus() == 201) {
                return "Usuário criado no Keycloak com sucesso!";
            } else {
                String errorMessage = response.readEntity(String.class);
                return "Erro ao criar usuário: " + response.getStatus() + " - " + errorMessage;
            }
        } catch (Exception e) {
            return("Erro ao criar user: " + e.getMessage());
        }
    }

    private static UserRepresentation getUserRepresentation(String username, String email, String password) {
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
}
