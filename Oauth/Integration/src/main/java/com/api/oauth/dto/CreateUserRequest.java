package com.api.oauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, message = "A senha deve ter ao menos 8 caracteres") String password) {
}
