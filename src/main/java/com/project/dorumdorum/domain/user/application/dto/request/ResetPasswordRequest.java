package com.project.dorumdorum.domain.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @Email @NotBlank String email,
        @NotBlank String newPassword,
        @NotBlank String newPasswordCheck
) {
    public boolean isPasswordMatch() {
        return newPassword.equals(newPasswordCheck);
    }
}
