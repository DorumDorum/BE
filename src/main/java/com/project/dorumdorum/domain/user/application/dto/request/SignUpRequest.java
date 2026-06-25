package com.project.dorumdorum.domain.user.application.dto.request;

import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.global.exception.RestApiException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus.AGE_PARSING_ERROR;

public record SignUpRequest(
        @NotBlank String name,
        @NotBlank String nickname,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.") @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$", message = "비밀번호는 영문, 숫자, 특수문자만 사용할 수 있습니다.") String password,
        @NotBlank String passwordCheck,
        @NotNull Gender gender,
        @NotBlank String studentNo,
        @NotBlank String major,
        @NotBlank String grade,
        @NotBlank String birth
) {
    public boolean isCheckedPassword() {
        return password().equals(passwordCheck());
    }

    public int calculateAge() {
        try {
            LocalDate birthDate = LocalDate.parse(birth, DateTimeFormatter.ISO_DATE);
            LocalDate now = LocalDate.now();
            return now.getYear() - birthDate.getYear() + 1;
        } catch (Exception e) {
            throw new RestApiException(AGE_PARSING_ERROR);
        }
    }
}
