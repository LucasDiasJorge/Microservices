package com.api.oauth.services;

import com.api.oauth.dto.TokenInfo;
import com.api.oauth.dto.TokenResponse;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);

    private final Keycloak keycloak;
    private final NimbusJwtDecoder jwtDecoder;
    private final String clientId;
    private final String clientSecret;
    private final String serverUrl;
    private final String realm;
    private final String issuer;

    /**
     * @param serverUrl endereço usado para FALAR com o Keycloak (backchannel).
     *                  Em Docker é o nome do serviço, ex.: http://keycloak:8080
     * @param issuerUri issuer que aparece DENTRO do token (frontchannel), ex.: http://localhost:8080/realms/my-realm.
     *                  Os dois divergem quando o Keycloak roda em container e o cliente
     *                  acessa por localhost; validar contra o backchannel rejeitaria todo token.
     *                  Vazio = deriva de serverUrl (caso de execução local sem Docker).
     */
    public KeycloakService(@Value("${keycloak.server-url}") String serverUrl,
                           @Value("${keycloak.issuer-uri:}") String issuerUri,
                           @Value("${keycloak.client-id}") String clientId,
                           @Value("${keycloak.client-secret}") String clientSecret,
                           @Value("${keycloak.realm}") String realm) {
        this.serverUrl = serverUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.realm = realm;
        this.issuer = StringUtils.hasText(issuerUri) ? issuerUri : serverUrl + "/realms/" + realm;

        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();

        // As chaves são buscadas pelo backchannel; o issuer validado é o do token.
        // withJwkSetUri é lazy: o serviço sobe mesmo com o Keycloak ainda indisponível.
        this.jwtDecoder = NimbusJwtDecoder
                .withJwkSetUri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/certs")
                .build();
        this.jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
    }

    public void createUser(String username, String email, String password) {
        UserRepresentation user = buildUser(username, email, password);

        try (Response response = keycloak.realm(realm).users().create(user)) {
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                return;
            }
            String detail = response.readEntity(String.class);
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()),
                    "Falha ao criar usuário no Keycloak: " + detail);
        }
    }

    private UserRepresentation buildUser(String username, String email, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRealmRoles(Collections.singletonList("user"));
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

    /**
     * O cliente por usuário é fechado ao final: a versão anterior criava um
     * Keycloak novo a cada login e nunca o fechava, vazando o pool de conexões.
     */
    public TokenResponse getToken(String username, String password) {
        try (Keycloak userClient = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .grantType("password")
                .build()) {

            AccessTokenResponse token = userClient.tokenManager().getAccessToken();
            return new TokenResponse(token.getToken(), token.getTokenType(), token.getExpiresIn());

        } catch (Exception e) {
            // Loga a causa real: um catch mudo aqui transforma qualquer falha
            // (rede, config, cliente mal configurado) num 401 enganoso.
            logger.warn("Falha ao obter token para o usuário {}: {}", username, e.toString(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", e);
        }
    }

    /**
     * Validação real: o NimbusJwtDecoder confere a assinatura contra o JWKS do
     * realm, além de issuer e expiração.
     *
     * A implementação anterior usava TokenVerifier sem chave pública e chamava
     * getToken(), que apenas faz o parse — nem os checks declarados rodavam.
     * Na prática, um JWT forjado passava.
     */
    public TokenInfo validateToken(String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;

        try {
            Jwt jwt = jwtDecoder.decode(token);
            return new TokenInfo(
                    jwt.getId(),
                    jwt.getSubject(),
                    jwt.getIssuer() != null ? jwt.getIssuer().toString() : null,
                    jwt.getExpiresAt(),
                    jwt.getAudience() != null ? jwt.getAudience() : List.of());
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido: " + e.getMessage(), e);
        }
    }
}
