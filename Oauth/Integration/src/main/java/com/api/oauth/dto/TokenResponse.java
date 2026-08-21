package com.api.oauth.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
}
