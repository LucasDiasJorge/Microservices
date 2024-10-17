package com.api.oauth.responses;

import java.util.Date;

public class ApiAuthResponse<T> {

    private boolean success;
    private String message;
    private String token;
    private Date expiresAt;

    public ApiAuthResponse(boolean success, String message, String token, Date expiresAt) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
