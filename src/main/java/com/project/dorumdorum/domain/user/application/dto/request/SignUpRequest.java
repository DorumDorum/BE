package com.project.dorumdorum.domain.user.application.dto.request;

import com.project.dorumdorum.domain.user.domain.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @NotBlank String name,
        @NotBlank String nickname,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String passwordCheck,
        @NotNull Gender gender,
        @NotBlank String studentNo,
        @NotBlank String major,
        @NotBlank String grade,
        @NotNull Integer age
) {
    public boolean isCheckedPassword() {
        return password().equals(passwordCheck());
    }

}
