package com.project.dorumdorum.domain.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.") @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$", message = "비밀번호는 영문, 숫자, 특수문자만 사용할 수 있습니다.") String newPassword,
        @NotBlank String newPasswordCheck
) {
    public boolean isPasswordMatch() {
        return newPassword.equals(newPasswordCheck);
    }
}
