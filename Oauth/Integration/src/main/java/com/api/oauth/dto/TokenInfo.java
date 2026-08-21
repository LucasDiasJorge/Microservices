package com.api.oauth.dto;

import java.time.Instant;
import java.util.List;

public record TokenInfo(String id, String subject, String issuer, Instant expiresAt, List<String> audience) {
}
