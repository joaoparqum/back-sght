package com.api.casadoconstrutor.sght.dto;

import com.api.casadoconstrutor.sght.user.UserRole;
import jakarta.validation.constraints.NotBlank;

public record RegisterDto(
        @NotBlank String login,
        @NotBlank String password,
        UserRole role
) { }
