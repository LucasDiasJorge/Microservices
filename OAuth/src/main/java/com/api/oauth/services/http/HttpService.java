package com.api.oauth.services.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class HttpService {

    protected RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger("HttpServiceLogger");

    public HttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void logRequest(HttpMethod method, String url, HttpEntity<?> requestEntity) {
        logger.info("Sending {} request to {} with headers: {} and body: {}",
                method, url, requestEntity.getHeaders(), requestEntity.getBody());
    }

    private void logResponse(ResponseEntity<?> responseEntity) {
        logger.info("Received response with status code: {} and body: {}",
                responseEntity.getStatusCode(), responseEntity.getBody());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("User-Agent", "application/x-www-form-urlencoded");
        return headers;
    }

    public Object postFormUrlEncoded(String url, String clientId, String clientSecret,
                                                  String scope, String grantType, String username,
                                                  String password) {
        HttpHeaders headers = createHeaders();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("scope", scope);
        body.add("grant_type", grantType);
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class).getBody();
    }
}
